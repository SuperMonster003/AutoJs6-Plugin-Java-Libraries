"""Read-only checks for the repository's Android/plugin engineering contract."""
from pathlib import Path
import argparse
import json
import re
import subprocess
import sys
import xml.etree.ElementTree as ET
import unicodedata

def non_ascii_punctuation(text):
    """Non-ASCII punctuation, arrows, the ideographic space and full-width forms (COMMON_AGENTS.md);
    U+00B7 stays allowed as a numeric separator. Covers every language, not only CJK marks."""
    return [ch for ch in text if ord(ch) > 0x7F and ch != '\u00b7' and (
        unicodedata.category(ch).startswith('P') or 0x2190 <= ord(ch) <= 0x21FF
        or ord(ch) == 0x3000 or 0xFF01 <= ord(ch) <= 0xFF65)]

ROOT = Path(__file__).resolve().parents[1]
ANDROID = '{http://schemas.android.com/apk/res/android}'
BUILD_TOLERANCE = 10
LOCALES = ['values', 'values-en', 'values-ar', 'values-es', 'values-fr', 'values-ja',
           'values-ko', 'values-ru', 'values-zh', 'values-zh-rHK', 'values-zh-rTW']

def check(root, pending_commit=False):
    errors = []
    def require(condition, message):
        if not condition: errors.append(message)
    props = dict(re.findall(r'^([A-Z_]+)=(.*)$', (root/'version.properties').read_text(encoding='utf-8-sig'), re.M))
    count = int(subprocess.check_output(['git','-C',str(root),'rev-list','--count','HEAD']))
    # VERSION_BUILD follows the commit count within a shared tolerance (D:/idea-projects/COMMON_AGENTS.md).
    drift = int(props['VERSION_BUILD']) - (count + int(pending_commit))
    require(abs(drift) <= BUILD_TOLERANCE, f'VERSION_BUILD must stay within {BUILD_TOLERANCE} of the commit count (off by {drift})')
    settings = (root/'settings.gradle.kts').read_text(encoding='utf-8-sig')
    # Any published release is accepted; shared plugins declared here must use one version.
    versions = re.findall(r'autojs6-(platform-versions|native-alignment)"\) version "(\d+\.\d+\.\d+)"', settings)
    require('platform-versions' in [plugin for plugin, _ in versions], 'platform-versions must come from a published release')
    require(len({version for _, version in versions}) == 1, 'Shared plugin versions must match')
    require('mavenLocal(' not in settings and 'autojs.buildPlugins.includeBuild' not in settings, 'Local platform substitution is forbidden')
    root_name = re.search(r'rootProject.name\s*=\s*"([^"]+)"', settings)
    require(root_name and re.fullmatch('[a-z0-9-]+', root_name[1]), 'rootProject.name must be lowercase kebab-case')
    require(not (root/'gradle/data').exists(), 'Consumer compatibility overrides must be removed')
    for relative in ['LICENSE','app/src/main/res/mipmap/ic_launcher.png', '.python/check_markdown.bat']:
        require((root/relative).is_file(), 'Missing ' + relative)
    for relative in ['AGENTS.md','CLAUDE.md','sign.properties','app/sm003.jks','local.properties','migration.pre-platform-versions.bak']:
        require(subprocess.run(['git','-C',str(root),'check-ignore','-q',relative]).returncode == 0, 'Not ignored: ' + relative)
    manifest = ET.parse(root/'app/src/main/AndroidManifest.xml').getroot()
    app = manifest.find('application')
    require(app.get(ANDROID+'allowBackup') == 'false', 'Backup must be disabled')
    require(app.get(ANDROID+'label') == '@string/app_name', 'Application label must use app_name')
    metadata = {e.get(ANDROID+'name'): e.get(ANDROID+'value') for e in app.findall('meta-data')}
    wake = metadata.get('org.autojs.plugin.WAKE_ACTIVITY')
    activities = [e for e in app.findall('activity') if e.get(ANDROID+'name') == wake]
    require(len(activities) == 1, 'Wake metadata must identify one activity')
    if activities:
        activity = activities[0]
        require(activity.get(ANDROID+'exported') == 'true', 'Wake must be exported')
        require(activity.get(ANDROID+'permission') == 'org.autojs.permission.PLUGIN', 'Wake must require plugin permission')
        require(activity.get(ANDROID+'theme') == '@android:style/Theme.NoDisplay', 'Wake must use NoDisplay')
        filters = activity.findall('intent-filter')
        require(any(any(a.get(ANDROID+'name') == 'org.autojs.plugin.action.WAKE' for a in f.findall('action'))
                    and any(c.get(ANDROID+'name') == 'android.intent.category.DEFAULT' for c in f.findall('category')) for f in filters), 'Wake action/category missing')
    english = None
    for locale in LOCALES:
        path = root/'app/src/main/res'/locale/'strings.xml'
        require(path.is_file(), 'Missing language ' + locale)
        if not path.exists(): continue
        resource = ET.parse(path).getroot()
        strings = resource.findall('string')
        names = [e.get('name') for e in strings]
        require(names == sorted(names), 'Unsorted strings: ' + locale)
        values = {e.get('name'): ''.join(e.itertext()) for file in path.parent.glob('*.xml')
                  for e in ET.parse(file).getroot().findall('string') if e.get('translatable') != 'false'}
        require('plugin_description' in values, 'Missing description: ' + locale)
        require(not any(e.get('translatable') == 'false' or e.get('name') == 'app_name' for e in strings), 'Nontranslatable identity in strings.xml: ' + locale)
        require(not resource.findall('plurals') and not resource.findall('string-array'), 'Mixed resource types: ' + locale)
        require(not any(non_ascii_punctuation(v) for v in values.values()), 'Non-ASCII punctuation: ' + locale)
        require(not re.search(r'[.!?;,:]$', values.get('plugin_description','')), 'Description punctuation: ' + locale)
        if locale == 'values': english = values
        elif locale == 'values-en': require(values == english, 'Default and explicit English differ')
    require('简体中文' in (root/'README.md').read_text(encoding='utf-8-sig'), 'Root README must identify Simplified Chinese')
    require(not list((root/'.changelog').glob('*.md')) or all(p.name.startswith('template') for p in (root/'.changelog').glob('*.md')), 'Generated changelogs must not live in .changelog')
    errors.extend(check_java_packages(root, manifest, app, metadata))
    errors.extend(check_punctuation(root))
    return errors

PLUGIN_PERMISSION = 'org.autojs.permission.PLUGIN'
EXPORTS_META_DATA = 'org.autojs.plugin.javaPackages.EXPORTS'
CONTRACT = 'app/src/main/java/io/github/supermonster003/autojs6/plugin/java/libraries/JavaLibrariesContract.kt'
# Text that is not ours to rewrite: the Icon Studio runtime shared by every plugin, Gradle's own scripts and the license
# text (COMMON_AGENTS.md, exceptions of the ASCII punctuation rule).
PUNCTUATION_EXEMPT = ('.icons/assets/', '.python/icon_studio_runtime.py', 'gradlew', 'gradlew.bat', 'LICENSE')
TEXT_SUFFIXES = {'.md', '.json', '.kt', '.kts', '.xml', '.py', '.yml', '.toml', '.properties', '.pro', '.svg', '.bat', ''}

def check_java_packages(root, manifest, app, metadata):
    """The contract with AutoJs6 (ROADMAP.md D3, D8 - D10, D17): the exported packages in the meta-data, the contract and
    the keep rules, no permission besides the plugin one, the info service, and one minimum host build in the build
    script and the documents."""
    errors = []
    def require(condition, message):
        if not condition: errors.append(message)
    contract = (root/CONTRACT).read_text(encoding='utf-8')
    declared = re.search(r'EXPORTED_PACKAGES = listOf\(([^)]*)\)', contract)
    packages = re.findall(r'"([^"]+)"', declared[1]) if declared else []
    require(packages, 'JavaLibrariesContract.EXPORTED_PACKAGES is missing')
    require(metadata.get(EXPORTS_META_DATA) == ','.join(packages), f'{EXPORTS_META_DATA} must list {packages} in this order')
    require(f'const val META_DATA_EXPORTS = "{EXPORTS_META_DATA}"' in contract, 'JavaLibrariesContract.META_DATA_EXPORTS must name the EXPORTS meta-data')
    rules = (root/'app/proguard-rules.pro').read_text(encoding='utf-8')
    kept = re.findall(r'^-keep class ([\w.]+)\.\*\* \{ \*; \}$', rules, re.M)
    require(sorted(set(kept) - {'org.autojs.plugin'}) == sorted(packages), f'proguard-rules.pro must keep exactly the exported packages {packages}')
    require(re.search(r'^-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisible\*Annotations, AnnotationDefault$', rules, re.M),
            'proguard-rules.pro must keep the attributes Jackson reads (ROADMAP.md D10)')
    require(metadata.get('requiresHostVersion') == '${requiredHostVersion}', 'Application requiresHostVersion must use the build placeholder')
    permissions = [e.get(ANDROID+'name') for e in manifest.findall('uses-permission')]
    require(permissions == [PLUGIN_PERMISSION], 'The plugin asks for no permission besides the plugin one (ROADMAP.md D3)')
    require(not app.findall('activity-alias') and not app.findall('provider') and not app.findall('receiver'), 'Unexpected aliases, providers or receivers')
    services = [s for s in app.findall('service') if s.get(ANDROID+'name') == '.JavaLibrariesInfoService']
    require(len(services) == 1 and len(app.findall('service')) == 1, 'The info service must be the only service')
    for service in services:
        require(service.get(ANDROID+'permission') == PLUGIN_PERMISSION and service.get(ANDROID+'exported') == 'true', 'The info service must be exported with the plugin permission')
        require(any(any(a.get(ANDROID+'name') == 'org.autojs.plugin.INFO' for a in f.findall('action'))
                    and any(c.get(ANDROID+'name') == 'java-libraries' for c in f.findall('category'))
                    for f in service.findall('intent-filter')), 'The info service must answer org.autojs.plugin.INFO in category java-libraries')
    for component in [*app.findall('activity'), *app.findall('service')]:
        launcher = any(any(c.get(ANDROID+'name') == 'android.intent.category.LAUNCHER' for c in f.findall('category')) for f in component.findall('intent-filter'))
        if component.get(ANDROID+'exported') == 'true' and not launcher:
            require(component.get(ANDROID+'permission') == PLUGIN_PERMISSION, f'Exported {component.get(ANDROID+"name")} must require the plugin permission')
    build = (root/'app/build.gradle.kts').read_text(encoding='utf-8')
    host = re.search(r'val requiredHostVersionCode = (\d+)L', build)
    common = json.loads((root/'.readme/common.json').read_text(encoding='utf-8'))
    require(host and common['required_host_version_code'] == host[1], 'common.json required_host_version_code must match requiredHostVersionCode of app/build.gradle.kts')
    require(common.get('exported_packages') == ', '.join(f'`{p}`' for p in packages), 'common.json exported_packages must list the exported packages')
    for constant, key in (('PLUGIN_ID', 'plugin_id'), ('ENGINE', 'plugin_engine')):
        require(f'const val {constant} = "{common[key]}"' in contract, f'JavaLibrariesContract.{constant} must match common.json {key}')
        require(f'resValue("string", "{key}", "{common[key]}")' in build, f'resValue {key} of app/build.gradle.kts must match common.json {key}')
    require('resValue("string", "plugin_variant", "default")' in build, 'resValue plugin_variant must be "default"')
    require('resValue("string", "plugin_requires_host_version", "$requiredHostVersionCode")' in build, 'resValue plugin_requires_host_version must use requiredHostVersionCode')
    return errors

def check_punctuation(root):
    """ASCII punctuation in every tracked text file of ours, in all languages (COMMON_AGENTS.md)."""
    errors = []
    files = subprocess.check_output(['git', '-C', str(root), 'ls-files', '--cached', '--others', '--exclude-standard'], text=True).splitlines()
    for relative in files:
        path = root/relative
        if relative.startswith(PUNCTUATION_EXEMPT) or relative in PUNCTUATION_EXEMPT or path.suffix not in TEXT_SUFFIXES or not path.is_file():
            continue
        try:
            text = path.read_text(encoding='utf-8')
        except UnicodeDecodeError:
            continue
        found = sorted(set(non_ascii_punctuation(text)))
        if found:
            errors.append(f'Non-ASCII punctuation in {relative}: ' + ' '.join(f'U+{ord(ch):04X}' for ch in found))
    return errors

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--pending-commit', action='store_true', help='expect the next commit number during review')
    args = parser.parse_args()
    errors = check(ROOT, args.pending_commit)
    print(json.dumps({'repository': ROOT.name, 'errors': errors}, ensure_ascii=False, indent=2))
    return bool(errors)

if __name__ == '__main__':
    sys.exit(main())
