"""Release smoke on a device (ROADMAP.md P3.4 step 8): Plugin Center lists the released plugin from the official index, the
APK installed from Plugin Center is the release asset, and the example of the plugin page runs in AutoJs6.

    py -3.12 -I .python/device_release_smoke.py -s SERIAL --release-apk RELEASE.apk --work DIR [--wait 900]

The installed host must be 6641 or later; it is not replaced here (device_plugin_center_check.py --host-apk shows how to
replace it for a while). The plugin must not be installed. The check removes the index cache of the host (run-as, so a
debug host), opens Plugin Center, waits until the new cache has the entry of the plugin (reopening every 90 s while the
online index lags) and compares it with version.properties, REQUIRED_HOST and the release
APK (title, versionName, versionCode, requiresHostVersion, icon, APK digest and size), saves a screenshot to DIR, then waits
up to --wait seconds while someone downloads and installs the plugin in Plugin Center by hand (the system install
confirmation is a human step). The Plugin Center switch of the plugin must then be on within 60 s (a fresh installation
writes it off first and the auto-enable of Plugin Center writes it on again, so the host is not stopped before), and the
installed APK must equal --release-apk. Last, JavaLibraries.EXAMPLE (the "Use in
scripts" example of the plugin page) runs in the host together with a lookup of the classes of ROADMAP.md appendix C.
The plugin is left installed; the device directory is removed.

Prints one PASS / FAIL line per item with the key values (no screen text) and returns 1 on a failure.
zh-CN: 发布冒烟 (P3.4 第 8 步): 插件中心从官方索引列出已发布的插件, 从插件中心安装的 APK 与发布资源一致, 插件页面的示例在
AutoJs6 中运行通过. 下载与系统安装确认由人在设备上操作. 每项输出一行 PASS / FAIL 与关键数值, 有失败时返回 1.
"""
import argparse
import hashlib
import json
import pathlib
import re
import subprocess
import sys
import time

HERE = pathlib.Path(__file__).resolve().parent
sys.path.insert(0, str(HERE))
import device_plugin_center_check as pc  # noqa: E402

ROOT = HERE.parent
CACHE = "files/plugin_center/autojs6_plugin_index_v11.json"
ENABLE_STORE = "shared_prefs/plugin_center_enable_state.xml"
SETTLE = 60
DEVICE_DIR = "/sdcard/Download/aj6-java-libraries-smoke"
MARK = "AJ6JLS"
TITLE = "Java Libraries"
APPENDIX_C = [
    "org.apache.commons.lang3.StringUtils", "org.apache.commons.lang3.time.DateFormatUtils",
    "org.apache.commons.lang3.builder.ToStringBuilder", "org.apache.commons.io.IOUtils", "org.apache.commons.io.FileUtils",
    "org.apache.commons.io.FilenameUtils", "com.fasterxml.jackson.databind.ObjectMapper",
    "com.fasterxml.jackson.databind.JsonNode", "com.fasterxml.jackson.core.JsonFactory",
    "com.fasterxml.jackson.annotation.JsonProperty",
]
EXPECTED_EXAMPLE = ["true", "Autojs6", "hello", "6", '{"name":"AutoJs6","version":6}']


def properties():
    values = {}
    for line in (ROOT / "version.properties").read_text(encoding="utf-8").splitlines():
        if "=" in line and not line.startswith("#"):
            key, value = line.split("=", 1)
            values[key.strip()] = value.strip()
    return values


def example():
    """JavaLibraries.EXAMPLE as the plugin page shows it (trimIndent of the raw string)."""
    source = (ROOT / "app/src/main/java/io/github/supermonster003/autojs6/plugin/java/libraries/JavaLibraries.kt")
    match = re.search(r'val EXAMPLE = """\n(.*?)\n\s*""".trimIndent\(\)', source.read_text(encoding="utf-8"), re.S)
    if not match:
        sys.exit("JavaLibraries.EXAMPLE not found")
    lines = match.group(1).splitlines()
    indent = min(len(l) - len(l.lstrip()) for l in lines if l.strip())
    return "\n".join(l[indent:] for l in lines)


def script():
    """The example with each console.log marked, then the appendix C lookup."""
    body = example().replace("console.log(", f"console.log('{MARK} out ' + ")
    classes = json.dumps(APPENDIX_C)
    return f"""try {{
{body}
    var names = {classes};
    var missing = [];
    names.forEach(function (name) {{
        try {{ if (typeof eval(name) !== 'function') missing.push(name); }} catch (e) {{ missing.push(name); }}
    }});
    console.log('{MARK} classes ' + (names.length - missing.length) + '/' + names.length + (missing.length ? ' missing ' + missing.join(',') : ''));
}} catch (e) {{
    console.log('{MARK} error ' + String(e).split('\\n')[0]);
}}
console.log('{MARK} done 1');
"""


OPEN_CENTER = """var ctx = org.autojs.autojs.app.GlobalAppContext.get();
var open = new android.content.Intent(ctx, ctx.getClassLoader().loadClass('org.autojs.autojs.core.plugin.center.PluginCenterActivity'));
open.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
ctx.startActivity(open);
"""


def push_and_run(serial, work, name, text):
    local = work / name
    local.write_text(text, encoding="utf-8")
    pc.adb(serial, "shell", "mkdir", "-p", DEVICE_DIR, check=True)
    pc.adb(serial, "push", str(local), f"{DEVICE_DIR}/{name}", check=True)
    pc.adb(serial, "logcat", "-c")
    pc.adb(serial, "shell", "am", "start", "-n", f"{pc.HOST}/{pc.RUN_ACTIVITY}", "-e", "path", f"{DEVICE_DIR}/{name}",
           check=True)


def collect(serial, wait):
    outs, values = [], {}
    deadline = time.time() + wait
    while time.time() < deadline and "done" not in values:
        time.sleep(1)
        outs, values = [], {}
        for line in pc.console_lines(serial):
            match = re.search(MARK + r" (\S+) (.*)$", line)
            if match:
                if match.group(1) == "out":
                    outs.append(match.group(2).strip())
                else:
                    values[match.group(1)] = match.group(2).strip()
    return outs, values


def enable_record(serial):
    """(enabled, suspended) of the plugin in the Plugin Center store, "-" for no record (enabled by default)."""
    text = pc.adb(serial, "exec-out", "run-as", pc.HOST, "cat", ENABLE_STORE).stdout
    values = []
    for kind in ("enabled", "suspended"):
        match = re.search(rf'name="key_\$_{kind}_plugin_\$_{re.escape(pc.PLUGIN)}" value="(\w+)"', text)
        values.append(match.group(1) if match else "-")
    return tuple(values)


def record_on(state):
    return state[0] != "false" and state[1] != "true"


def cached_entry(serial):
    result = pc.adb(serial, "exec-out", "run-as", pc.HOST, "cat", CACHE)
    try:
        items = json.loads(result.stdout).get("items", [])
    except ValueError:
        return None
    return next((item for item in items if item.get("packageName") == pc.PLUGIN), None)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-s", "--serial", required=True)
    parser.add_argument("--release-apk", required=True, type=pathlib.Path)
    parser.add_argument("--work", required=True, type=pathlib.Path)
    parser.add_argument("--wait", type=int, default=900, help="seconds to wait for the entry and for the install")
    args = parser.parse_args()
    serial, work = args.serial, args.work
    work.mkdir(parents=True, exist_ok=True)
    props = properties()
    release = args.release_apk.read_bytes()
    release_sha256 = hashlib.sha256(release).hexdigest()
    results = []

    def report(name, ok, detail):
        results.append(ok)
        print(f"{'PASS' if ok else 'FAIL'} {name}: {detail}", flush=True)

    host = pc.version_code(serial, pc.HOST)
    report("host version", host is not None and host >= pc.REQUIRED_HOST, f"{host} (required {pc.REQUIRED_HOST})")
    if pc.installed(serial, pc.PLUGIN):
        sys.exit("the plugin is installed; uninstall it first so that Plugin Center installs the release")
    try:
        deadline = time.time() + args.wait
        entry, opened = None, 0.0
        while time.time() < deadline and entry is None:
            if time.time() - opened > 90:
                # The host keeps the index for an hour; drop the cache so that Plugin Center reads the index online.
                pc.adb(serial, "shell", "am", "force-stop", pc.HOST)
                pc.adb(serial, "shell", "run-as", pc.HOST, "rm", "-f", CACHE)
                pc.start_host(serial)
                push_and_run(serial, work, "open_plugin_center.js", OPEN_CENTER)
                opened = time.time()
            time.sleep(5)
            entry = cached_entry(serial)
        if entry is None:
            report("index entry", False, f"not in the host index cache after {args.wait} s")
            return 1
        latest = (entry.get("releases") or [{}])[0]
        report("entry title", entry.get("title") == TITLE, entry.get("title"))
        report("entry version", latest.get("versionName") == props["VERSION_NAME"]
               and latest.get("versionCode") == int(props["VERSION_BUILD"]),
               f"{latest.get('versionName')} ({latest.get('versionCode')})")
        report("entry requiresHostVersion", entry.get("requiresHostVersion") == pc.REQUIRED_HOST,
               entry.get("requiresHostVersion"))
        icon = entry.get("iconUrl") or ""
        report("entry icon", icon.endswith(".png") and "icons/" + pc.PLUGIN in icon, icon.rsplit("/", 1)[-1][:16])
        report("entry APK", latest.get("apkSha256") == release_sha256 and latest.get("apkSizeBytes") == len(release),
               f"sha256 {str(latest.get('apkSha256'))[:8]}, {latest.get('apkSizeBytes')} bytes")
        shot = work / "plugin-center.png"
        with shot.open("wb") as f:
            subprocess.run(["adb", "-s", serial, "exec-out", "screencap", "-p"], stdout=f, timeout=60)
        print(f"screenshot: {shot}", flush=True)
        print(f"WAIT install {TITLE} from Plugin Center (up to {args.wait} s)", flush=True)
        deadline = time.time() + args.wait
        while time.time() < deadline and not pc.installed(serial, pc.PLUGIN):
            time.sleep(1)
        if not pc.installed(serial, pc.PLUGIN):
            report("installed from Plugin Center", False, f"not installed after {args.wait} s")
            return 1
        # Right after a fresh installation Plugin Center writes the switch off and its auto-enable after the installation
        # writes it on again; the host must keep running until then, or the plugin stays off.
        # zh-CN: 全新安装后插件中心先把开关写为关闭, 安装后的自动启用随后再写为打开; 在此之前不能结束宿主, 否则插件保持关闭.
        installed_at = time.time()
        state = enable_record(serial)
        while time.time() - installed_at < SETTLE and not record_on(state):
            time.sleep(1)
            state = enable_record(serial)
        report("enabled after install", record_on(state),
               f"enabled={state[0]}, suspended={state[1]} after {time.time() - installed_at:.0f} s")
        paths = [l[len("package:"):] for l in pc.shell(serial, f"pm path {pc.PLUGIN}").split() if l.startswith("package:")]
        remote = pc.shell(serial, f"sha256sum '{paths[0]}'").split()[0] if len(paths) == 1 else "-"
        report("installed APK is the release", remote == release_sha256,
               f"versionCode {pc.version_code(serial, pc.PLUGIN)}, sha256 {remote[:8]}")
        pc.start_host(serial)
        push_and_run(serial, work, "release_smoke.js", script())
        outs, values = collect(serial, 90)
        if "error" in values:
            report("example", False, values["error"])
        report("example output", outs == EXPECTED_EXAMPLE, f"{sum(a == b for a, b in zip(outs, EXPECTED_EXAMPLE))}/"
               f"{len(EXPECTED_EXAMPLE)} lines as commented")
        report("appendix C classes", values.get("classes") == f"{len(APPENDIX_C)}/{len(APPENDIX_C)}",
               values.get("classes", "no output"))
    finally:
        pc.adb(serial, "shell", "rm", "-rf", DEVICE_DIR)
    return 0 if all(results) else 1


if __name__ == "__main__":
    sys.exit(main())
