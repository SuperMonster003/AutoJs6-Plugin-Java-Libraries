"""Device check: AutoJs6 discovers this plugin, lists it in Plugin Center and counts it as enabled by default.

    py -3.12 -I .python/device_plugin_center_check.py -s SERIAL --plugin-apk PLUGIN.apk --work DIR [--host-apk HOST.apk]

The plugin needs host 6640 or later (requiresHostVersion, ROADMAP.md D17). On an older host Plugin Center lists it as
needing a newer host and stores it as switched off, so give --host-apk (a debuggable AutoJs6 build of that version or
later, signed like the installed one): the installed host is pulled to DIR, replaced for the check and installed back at
the end, with the MD5 of the device copy compared to the backup.

Steps: install the plugin, start the host launcher activity, run a script in the host (RunIntentActivity) that resolves
the INFO service through the package manager, reads the signer state and the default enable policy, opens Plugin Center,
waits for its discovery and reads the stored switch; a screenshot of Plugin Center is saved to DIR for a look. The plugin
is uninstalled at the end unless it was installed before, and the device directory is removed.

Prints one PASS / FAIL line per item with the key values (no screen text) and returns 1 on a failure.
zh-CN: 设备检查: 宿主发现本插件, 插件中心列出且默认启用. 每项输出一行 PASS / FAIL 与关键数值, 有失败时返回 1.
"""
import argparse
import hashlib
import pathlib
import re
import subprocess
import sys
import time

HOST = "org.autojs.autojs6"
PLUGIN = "io.github.supermonster003.autojs6.plugin.java.libraries"
ENGINE = "java-libraries"
REQUIRED_HOST = 6640
RUN_ACTIVITY = "org.autojs.autojs.external.open.RunIntentActivity"
DEVICE_DIR = "/sdcard/Download/aj6-java-libraries-check"
MARK = "AJ6JL"

SCRIPT = f"""
var ctx = org.autojs.autojs.app.GlobalAppContext.get();
var pkg = '{PLUGIN}';
var center = org.autojs.autojs.core.plugin.center;
function out(key, value) {{ console.log('{MARK} ' + key + ' ' + value); }}
try {{
    var query = new android.content.Intent('org.autojs.plugin.INFO');
    query.addCategory('{ENGINE}');
    var found = ctx.getPackageManager().queryIntentServices(query, 0);
    var names = [];
    for (var i = 0; i < found.size(); i++) {{
        var info = found.get(i).serviceInfo;
        if (String(info.packageName) === pkg) names.push(String(info.name));
    }}
    out('discovered', names.join(',') || '-');
    out('authorized', center.PluginTrustManager.INSTANCE.resolveTrustInfo(ctx, pkg).getAuthorizedState());
    out('default-enabled', center.PluginDefaultEnabledPolicy.INSTANCE.isEnabledByDefault(ctx, '{ENGINE}', pkg));
    var open = new android.content.Intent(ctx, ctx.getClassLoader().loadClass('org.autojs.autojs.core.plugin.center.PluginCenterActivity'));
    open.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
    ctx.startActivity(open);
    sleep(10000);
    var store = center.PluginEnableStore.INSTANCE;
    var withTrue = store.isEnabled(ctx, pkg, true);
    var withFalse = store.isEnabled(ctx, pkg, false);
    out('stored', withTrue && !withFalse ? 'none' : String(withTrue));
}} catch (e) {{
    out('error', String(e).split('\\n')[0]);
}}
out('done', 1);
"""


def adb(serial, *args, check=False, timeout=300):
    result = subprocess.run(["adb", "-s", serial, *args], capture_output=True, text=True, encoding="utf-8",
                            errors="replace", timeout=timeout)
    if check and result.returncode != 0:
        sys.exit(f"adb {args[0]} failed: {(result.stderr or result.stdout).strip().splitlines()[-1:]}")
    return result


def shell(serial, command):
    return adb(serial, "shell", command).stdout.replace("\r", "")


def version_code(serial, package):
    match = re.search(r"versionCode=(\d+)", shell(serial, f"dumpsys package {package} | grep -m1 versionCode"))
    return int(match.group(1)) if match else None


def installed(serial, package):
    return f"package:{package}" in shell(serial, f"pm list packages {package}").split()


def device_md5(serial, path):
    return shell(serial, f"md5sum '{path}'").split()[0]


def host_apk_path(serial):
    lines = [l[len("package:"):] for l in shell(serial, f"pm path {HOST}").split() if l.startswith("package:")]
    if len(lines) != 1:
        sys.exit(f"the host is not one APK on the device: {len(lines)} paths")
    return lines[0]


def console_lines(serial):
    out = adb(serial, "logcat", "-d", "-t", "50000").stdout
    return [re.sub(r"^.*GlobalConsole: ", "", l).rstrip() for l in out.splitlines() if "GlobalConsole:" in l]


def run_script(serial, work, wait=60):
    local = work / "plugin_center_check.js"
    local.write_text(SCRIPT, encoding="utf-8")
    adb(serial, "shell", "mkdir", "-p", DEVICE_DIR, check=True)
    adb(serial, "push", str(local), f"{DEVICE_DIR}/{local.name}", check=True)
    adb(serial, "logcat", "-c")
    adb(serial, "shell", "am", "start", "-n", f"{HOST}/{RUN_ACTIVITY}", "-e", "path", f"{DEVICE_DIR}/{local.name}",
        check=True)
    values = {}
    deadline = time.time() + wait
    while time.time() < deadline and "done" not in values:
        time.sleep(1)
        for line in console_lines(serial):
            match = re.search(MARK + r" (\S+) (.*)$", line)
            if match:
                values[match.group(1)] = match.group(2).strip()
    return values


def start_host(serial):
    adb(serial, "shell", "am", "force-stop", HOST)
    activity = shell(serial, f"cmd package resolve-activity --brief -c android.intent.category.LAUNCHER {HOST}")
    component = activity.strip().splitlines()[-1]
    adb(serial, "shell", "am", "start", "-n", component, check=True)
    time.sleep(4)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("-s", "--serial", required=True)
    parser.add_argument("--plugin-apk", required=True, type=pathlib.Path)
    parser.add_argument("--host-apk", type=pathlib.Path)
    parser.add_argument("--work", required=True, type=pathlib.Path)
    args = parser.parse_args()
    serial, work = args.serial, args.work
    work.mkdir(parents=True, exist_ok=True)
    results = []

    def report(name, ok, detail):
        results.append(ok)
        print(f"{'PASS' if ok else 'FAIL'} {name}: {detail}")

    original_host = version_code(serial, HOST)
    backup = backup_md5 = None
    if args.host_apk:
        backup = work / "host-backup.apk"
        remote = host_apk_path(serial)
        adb(serial, "pull", remote, str(backup), check=True)
        backup_md5 = hashlib.md5(backup.read_bytes()).hexdigest()
        if backup_md5 != device_md5(serial, remote):
            sys.exit("the host backup does not match the device copy; nothing was changed")
        print(f"host backup: {original_host}, md5 {backup_md5[:8]}, {backup.stat().st_size} bytes")
        adb(serial, "install", "-r", "-t", str(args.host_apk), check=True)
    host = version_code(serial, HOST)
    report("host version", host is not None and host >= REQUIRED_HOST, f"{host} (required {REQUIRED_HOST})")

    had_plugin = installed(serial, PLUGIN)
    try:
        adb(serial, "install", "-r", "-t", str(args.plugin_apk), check=True)
        report("plugin installed", installed(serial, PLUGIN), f"versionCode {version_code(serial, PLUGIN)}")
        start_host(serial)
        values = run_script(serial, work)
        if "error" in values:
            report("host script", False, values["error"])
        report("INFO service resolved", values.get("discovered", "-") != "-", values.get("discovered", "no output"))
        report("signer authorized", values.get("authorized") in ("OFFICIAL", "TRUSTED"), values.get("authorized"))
        report("enabled by default", values.get("default-enabled") == "true", values.get("default-enabled"))
        report("switch after Plugin Center", values.get("stored") in ("none", "true"), values.get("stored"))
        shot = work / "plugin-center.png"
        with shot.open("wb") as f:
            subprocess.run(["adb", "-s", serial, "exec-out", "screencap", "-p"], stdout=f, timeout=60)
        print(f"screenshot: {shot}")
    finally:
        if not had_plugin:
            adb(serial, "uninstall", PLUGIN)
            report("plugin removed", not installed(serial, PLUGIN), "uninstalled")
        adb(serial, "shell", "rm", "-rf", DEVICE_DIR)
        if backup:
            adb(serial, "install", "-r", "-d", "-t", str(backup), check=True)
            restored = device_md5(serial, host_apk_path(serial))
            report("host restored", restored == backup_md5 and version_code(serial, HOST) == original_host,
                   f"{version_code(serial, HOST)}, md5 {restored[:8]}")
    return 0 if all(results) else 1


if __name__ == "__main__":
    sys.exit(main())
