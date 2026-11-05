"""Rehearse the official plugin index entry of this repository before it is published (ROADMAP.md P3.2 / P3.4).

The official index generator (AutoJs6-Official-Plugins-Index, tools/generate_official_plugin_index.py) reads a
published GitHub release and the source tree of its tag. This script feeds the same entry builder with the HEAD
commit of this repository and a synthetic release (one universal APK and a matching admission manifest), so the
metadata the generator derives can be checked before the repository exists on GitHub.

Nothing is downloaded and nothing is written: the generator modules are read with `git show HEAD:<path>` from the
index checkout into a temporary directory, and catalog presentation only reads the index checkout. The synthetic
asset digest, size and signer are placeholders; the real values come from the signed release (ROADMAP.md P3.4).

usage: python .python/index_rehearsal.py [--index ../AutoJs6-Official-Plugins-Index]
"""
import argparse
import importlib
import json
import re
import subprocess
import sys
import tempfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OWNER = "SuperMonster003"
REPOSITORY = "AutoJs6-Plugin-Java-Libraries"
GENERATOR_MODULES = ("generate_official_plugin_index", "axml", "catalog_presentation", "native_page_alignment")
PLACEHOLDER_SHA256 = "ab" * 32
PLACEHOLDER_SIZE = 1234567
# The release signer of the official plugins (same as Bundled Libraries 1.0.0 and Pinyin4j 1.4.0 in the index).
OFFICIAL_SIGNER_SHA256 = "31a681fcfffb3e428420cae280ded89292b12a3b0f59e19b7a73e32a8ae4c213"


def git(repo: Path, *args: str) -> bytes:
    return subprocess.run(["git", "-C", str(repo), *args], check=True, capture_output=True).stdout


def load_generator(index: Path, workdir: Path):
    for name in GENERATOR_MODULES:
        (workdir / f"{name}.py").write_bytes(git(index, "show", f"HEAD:tools/{name}.py"))
    sys.dont_write_bytecode = True
    sys.path.insert(0, str(workdir))
    return importlib.import_module("generate_official_plugin_index"), importlib.import_module("catalog_presentation")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--index", type=Path, default=ROOT.parent / "AutoJs6-Official-Plugins-Index")
    args = parser.parse_args()
    index = args.index.resolve()
    common = json.loads((ROOT / ".readme/common.json").read_text(encoding="utf-8"))

    tree_paths = set(git(ROOT, "ls-tree", "-r", "--name-only", "HEAD").decode("utf-8").splitlines())
    head = git(ROOT, "rev-parse", "HEAD").decode("ascii").strip()

    def read_source(path):
        return git(ROOT, "show", f"HEAD:{path}").decode("utf-8") if path in tree_paths else None

    with tempfile.TemporaryDirectory() as workdir:
        generator, presentation = load_generator(index, Path(workdir))

        strings_by_dir: dict[str, dict[str, str]] = {}
        for path in sorted(tree_paths):
            match = re.fullmatch(r"app/src/main/res/(values(?:-[^/]+)?)/strings(?:_[^/]+)?\.xml", path)
            if match:
                bucket = strings_by_dir.setdefault(match.group(1), {})
                values = generator.parse_strings_xml(read_source(path))
                if bucket.keys() & values.keys():
                    raise SystemExit(f"duplicate string resources in {match.group(1)}: {sorted(bucket.keys() & values.keys())}")
                bucket.update(values)

        version_map = generator.parse_properties(read_source("version.properties") or "")
        manifest_text = read_source("app/src/main/AndroidManifest.xml")
        build_gradle = read_source("app/build.gradle.kts") or ""
        placeholders = generator.resolve_manifest_placeholders(
            build_gradle, manifest_text, read_source=read_source, version_map=version_map,
        )
        version_name = version_map["VERSION_NAME"]
        version_code = int(version_map["VERSION_BUILD"])
        tag = f"v{version_name}"
        asset_name = f"autojs6-plugin-java-libraries-v{version_name}-universal-0123abcd.apk"
        release = {
            "tag_name": tag,
            "name": tag,
            "published_at": "2026-01-01T00:00:00Z",
            "html_url": f"https://github.com/{OWNER}/{REPOSITORY}/releases/tag/{tag}",
            "body": "rehearsal",
            "author": {"login": OWNER},
            "prerelease": False,
            "draft": False,
            "assets": [{
                "name": asset_name,
                "browser_download_url": f"https://github.com/{OWNER}/{REPOSITORY}/releases/download/{tag}/{asset_name}",
                "size": PLACEHOLDER_SIZE,
                "digest": "sha256:" + PLACEHOLDER_SHA256,
            }],
        }
        admission = {
            "schemaVersion": 1,
            "owner": OWNER,
            "repository": REPOSITORY,
            "releaseTag": tag,
            "sourceCommit": head,
            "packageName": common["plugin_application_id"],
            "versionName": version_name,
            "versionCode": version_code,
            "signerSha256": [OFFICIAL_SIGNER_SHA256],
            "artifacts": [{"name": asset_name, "sha256": PLACEHOLDER_SHA256, "sizeBytes": PLACEHOLDER_SIZE}],
        }
        entries = generator.build_entries_from_release(
            owner=OWNER, repo_name=REPOSITORY, ref=f"refs/tags/{tag}", release=release,
            tree_paths=tree_paths, strings_by_dir=strings_by_dir, version_map=version_map,
            manifest_text=manifest_text, build_gradle=build_gradle, manifest_placeholders=placeholders,
            read_source=read_source, admission_root=None, admission_manifest_text=json.dumps(admission),
            source_commit=head, native_cache=None,
        )
        presentation.apply_catalog_presentation(entries, root=index)

    if len(entries) != 1:
        print(f"FAIL entries: expected 1, got {len(entries)}")
        return 1
    entry = entries[0]
    release_entry = entry["releases"][0]
    asset = release_entry["assets"][0]
    app_name = strings_by_dir.get("values", {}).get("app_name")
    checks = [
        ("packageName", entry["packageName"], common["plugin_application_id"]),
        ("title", entry["title"], app_name),
        ("author", entry["author"], OWNER),
        ("engine", entry.get("engine"), common["plugin_engine"]),
        ("variant", entry.get("variant"), "default"),
        ("engineId", entry.get("engineId"), common["plugin_id"]),
        ("requiresHostVersion", entry.get("requiresHostVersion"), int(common["required_host_version_code"])),
        ("featured", entry.get("featured"), True),
        ("versionName", release_entry["versionName"], version_name),
        ("versionCode", release_entry["versionCode"], version_code),
        ("nativePageAlignment", (release_entry.get("nativePageAlignment"), release_entry.get("nativePageAlignmentSource")), (0, "declared")),
        ("supportedAbis", entry.get("supportedAbis"), None),
        ("runtimeComponent", entry.get("runtimeComponent"), None),
        ("description languages", len(entry.get("localizedDescriptions", {})), 11),
        ("instruction page languages", len(entry.get("localizedInstructionMarkdownUrls", {})), 11),
        ("iconUrl", (entry.get("iconUrl") or "").rsplit(f"/{tag}/", 1)[-1], "app/src/main/res/mipmap/ic_plugin_center.png"),
        ("nightIconUrl", (entry.get("nightIconUrl") or "").rsplit(f"/{tag}/", 1)[-1], "app/src/main/res/mipmap-night/ic_plugin_center.png"),
        ("admitted sourceCommit", asset.get("sourceCommit"), head),
        ("admitted signer", asset.get("signerSha256"), [OFFICIAL_SIGNER_SHA256]),
        ("forceIgnoreLocalIcon", entry.get("forceIgnoreLocalIcon"), True),
    ]
    failed = 0
    for name, actual, expected in checks:
        ok = actual == expected
        failed += not ok
        print(f"{'PASS' if ok else 'FAIL'} {name}: {actual!r}" + ("" if ok else f" (expected {expected!r})"))
    print(f"RESULT {'PASS' if not failed else 'FAIL'} ({len(checks) - failed}/{len(checks)}); "
          f"index HEAD {git(index, 'rev-parse', '--short', 'HEAD').decode('ascii').strip()}, plugin HEAD {head[:7]}")
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())
