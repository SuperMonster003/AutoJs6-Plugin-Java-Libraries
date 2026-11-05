"""Device check of the launch pages (ROADMAP.md P0.4): the launch page and the release history page follow the language,
night mode and theme colors of AutoJs6, are laid out right to left in Arabic with the NOTICE files kept left to right, and
follow the system while AutoJs6 is disabled.

    py -3.12 -I .python/device_host_appearance_check.py -s SERIAL --apk app/build/outputs/apk/debug/<name>.apk [--shots DIR]

Needs a debuggable AutoJs6 on the device (its preferences are edited with run-as while it is stopped) and the debug build
of this plugin (ReleaseHistoryActivity is exported to adb in debug builds only). Never taps: the pages start with am start
and the launch page is scrolled with swipes; the results come from screencap pixels and uiautomator dump text and bounds.
Prints one line per check and PASS / FAIL (no screen text beyond the expected strings). The host preferences file is
restored byte for byte and AutoJs6 is enabled again at the end; the plugin is uninstalled unless it was installed before
(then it stays on the APK given).
zh-CN: 启动页外观设备检查 (ROADMAP.md P0.4): 两个页面跟随宿主语言, 夜间模式与主题色, 阿拉伯语下从右到左而 NOTICE 保持从左到右,
宿主停用时跟随系统. 不点按; 每项一行 PASS / FAIL; 宿主偏好按字节还原, 结束时重新启用宿主.
"""
import argparse
import hashlib
import pathlib
import re
import struct
import subprocess
import sys
import time
import xml.etree.ElementTree as ET
import zlib

HOST = "org.autojs.autojs6"
PLUGIN = "io.github.supermonster003.autojs6.plugin.java.libraries"
SETTINGS = f"{PLUGIN}/.SettingsActivity"
RELEASE_HISTORY = f"{PLUGIN}/.ReleaseHistoryActivity"
PREFS = f"shared_prefs/{HOST}_preferences.xml"
DUMP = "/data/local/tmp/java-libraries-appearance.xml"
SETTLE_SECONDS = 5
FIRST_LIBRARY = "Apache Commons Lang"
NOTICE_FIRST_LINE = "Apache Commons Lang"

# Host settings of each scenario and what the plugin pages must show. zh-CN: 各场景的宿主设置与插件页面应有的显示.
AR_DARK = {"language": "key_$_app_language_ar", "night": True, "rtl": True,
           "primary": 0xFF3F51B5, "primary_dark": 0xFF303F9F, "accent": 0xFFFF4081}
ZH_LIGHT = {"language": "key_$_app_language_zh", "night": False, "rtl": False,
            "primary": 0xFFFFDEAD, "primary_dark": 0xFFFFDEAD, "accent": 0xFFFFDEAD}


class Device:
    def __init__(self, serial):
        self.serial = serial

    def run(self, *args, check=True, stdin=None):
        result = subprocess.run(["adb", "-s", self.serial, *args], input=stdin, capture_output=True, check=False)
        if check and result.returncode != 0:
            raise RuntimeError(f"adb {' '.join(args)}: {result.stderr.decode(errors='replace').strip()}")
        return result.stdout

    def shell(self, command, check=True):
        return self.run("shell", command, check=check).decode(errors="replace")

    def read_prefs(self):
        return self.run("exec-out", "run-as", HOST, "cat", PREFS)

    def write_prefs(self, data):
        # exec-in passes its arguments without a shell, so the redirection goes through adb shell (stdin forwarded).
        self.run("shell", f'run-as {HOST} sh -c "cat > {PREFS}"', stdin=data)
        if self.read_prefs() != data:
            raise RuntimeError("the host preferences were not written")

    def screen(self):
        raw = self.run("exec-out", "screencap")
        width, height, _ = struct.unpack_from("<III", raw)
        header = len(raw) - width * height * 4
        return Screen(width, height, raw[header:])

    def dump(self):
        for _ in range(3):
            self.shell(f"uiautomator dump {DUMP}", check=False)
            text = self.run("exec-out", "cat", DUMP, check=False).decode(errors="replace")
            self.shell(f"rm -f {DUMP}", check=False)
            if text.startswith("<?xml"):
                return ET.fromstring(text)
            time.sleep(1)
        raise RuntimeError("uiautomator dump failed")

    def start(self, component, *flags):
        self.shell(f"am start -W {' '.join(flags)} -n {component}")
        time.sleep(SETTLE_SECONDS)

    def swipe_up(self, screen):
        x = screen.width // 2
        self.shell(f"input swipe {x} {screen.height * 3 // 4} {x} {screen.height // 3} 400")
        time.sleep(1.5)

    def stop_all(self):
        self.shell(f"am force-stop {PLUGIN}")
        self.shell(f"am force-stop {HOST}")


class Screen:
    def __init__(self, width, height, pixels):
        self.width, self.height, self.pixels = width, height, pixels

    def at(self, x, y):
        offset = (y * self.width + x) * 4
        r, g, b = self.pixels[offset:offset + 3]
        return 0xFF000000 | r << 16 | g << 8 | b

    def png(self, path):
        stride = self.width * 4
        rows = b"".join(bytes(1) + self.pixels[y * stride:(y + 1) * stride] for y in range(self.height))

        def chunk(kind, data):
            return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data))

        signature = bytes([0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A])
        header = struct.pack(">IIBBBBB", self.width, self.height, 8, 6, 0, 0, 0)
        path.write_bytes(signature + chunk(b"IHDR", header) + chunk(b"IDAT", zlib.compress(rows, 6)) + chunk(b"IEND", b""))

    def ink(self, box):
        """The pixels of [box] that differ from its most common color: (color farthest from it, ink rows by x extent)."""
        left, top, right, bottom = box
        counts = {}
        for y in range(top, bottom, 2):
            for x in range(left, right, 2):
                color = self.at(x, y)
                counts[color] = counts.get(color, 0) + 1
        background = max(counts, key=counts.get)
        strongest, strongest_distance, rows = None, 0, {}
        for y in range(top, bottom):
            for x in range(left, right):
                color = self.at(x, y)
                distance = max(abs((color >> s & 0xFF) - (background >> s & 0xFF)) for s in (16, 8, 0))
                if distance > 60:
                    low, high = rows.get(y, (x, x))
                    rows[y] = (min(low, x), max(high, x))
                    if distance > strongest_distance:
                        strongest, strongest_distance = color, distance
        return strongest, rows


def with_host_settings(prefs, settings):
    """[prefs] (the XML of the host preferences) with the language, night mode and theme colors of [settings]."""
    text = prefs.decode("utf-8")
    entries = {
        "key_$_app_language": ("string", settings["language"]),
        "key_$_night_mode": ("string", "key_$_night_mode_always_on" if settings["night"] else "key_$_night_mode_always_off"),
        "key_$_auto_night_mode_enabled": ("boolean", "false"),
        "key_$_night_mode_enabled": ("boolean", "true" if settings["night"] else "false"),
        "key_$_theme_color_primary": ("int", str(settings["primary"] - (1 << 32))),
        "key_$_theme_color_primary_dark": ("int", str(settings["primary_dark"] - (1 << 32))),
        "key_$_theme_color_accent": ("int", str(settings["accent"] - (1 << 32))),
    }
    for name, (kind, value) in entries.items():
        pattern = re.compile(r'\s*<(string|int|boolean) name="' + re.escape(name) + r'"(?: value="[^"]*" />|>[^<]*</string>)')
        text = pattern.sub("", text)
        element = f'<string name="{name}">{value}</string>' if kind == "string" else f'<{kind} name="{name}" value="{value}" />'
        text = text.replace("</map>", f"    {element}" + chr(10) + "</map>")
    return text.encode("utf-8")


def bounds(node):
    left, top, right, bottom = map(int, re.findall(r"\d+", node.get("bounds")))
    return left, top, right, bottom


def texts(root):
    return [node.get("text") for node in root.iter("node") if node.get("text")]


def node_with_text(root, text, prefix=False):
    for node in root.iter("node"):
        value = node.get("text") or ""
        if value.startswith(text) if prefix else value == text:
            return node
    return None


def action_bar(root):
    return next((node for node in root.iter("node") if node.get("resource-id") == "android:id/action_bar"), None)


def luminance(color):
    def channel(value):
        value /= 255
        return value / 12.92 if value <= 0.03928 else ((value + 0.055) / 1.055) ** 2.4

    return 0.2126 * channel(color >> 16 & 0xFF) + 0.7152 * channel(color >> 8 & 0xFF) + 0.0722 * channel(color & 0xFF)


def close(first, second, tolerance=6):
    return all(abs((first >> shift & 0xFF) - (second >> shift & 0xFF)) <= tolerance for shift in (16, 8, 0))


def hex_color(color):
    return f"#{color & 0xFFFFFF:06X}"


class Checks:
    def __init__(self):
        self.failed = 0

    def check(self, name, ok, detail):
        self.failed += not ok
        print(f"{'PASS' if ok else 'FAIL'} {name}: {detail}", flush=True)


def page_appearance(device, checks, name, shots, expect_texts, settings, heading=None):
    """Checks the page on screen; [settings] None means the system appearance (AutoJs6 unavailable)."""
    root = device.dump()
    screen = device.screen()
    if shots:
        screen.png(shots / f"{name}.png")
    found = texts(root)
    for expected in expect_texts:
        # "text*" is a part of a longer text, anything else a whole text. zh-CN: "text*" 为长文本的一部分, 其余为完整文本.
        shown = any(expected[:-1] in text if expected.endswith("*") else expected == text for text in found)
        checks.check(f"{name} text", shown, f"{expected.rstrip('*')!r} {'shown' if shown else 'missing'}")
    bar = action_bar(root)
    if bar is None:
        checks.check(f"{name} action bar", False, "android:id/action_bar not in the dump")
        return root, screen
    left, top, right, bottom = bounds(bar)
    # The end of the bar away from the title (the left in a right-to-left layout). zh-CN: 远离标题的一端.
    bar_x = left + 4 if settings and settings["rtl"] else right - 4
    bar_color = screen.at(bar_x, (top + bottom) // 2)
    # Just above the action bar: the top rows may carry the pointer location overlay of the developer options.
    status_color = screen.at(screen.width // 2, max(top - 4, 0))
    background = screen.at(screen.width - 3, (bottom + screen.height) // 2)
    if settings is None:
        checks.check(f"{name} action bar", bar_color not in (AR_DARK["primary"], ZH_LIGHT["primary"]), f"{hex_color(bar_color)} (theme default)")
        checks.check(f"{name} background", luminance(background) > 0.5, f"{hex_color(background)} light (system)")
        return root, screen
    checks.check(f"{name} action bar", close(bar_color, settings["primary"]), f"{hex_color(bar_color)} want {hex_color(settings['primary'])}")
    checks.check(f"{name} status bar", close(status_color, settings["primary_dark"]), f"{hex_color(status_color)} want {hex_color(settings['primary_dark'])}")
    dark = luminance(background) < 0.2
    checks.check(f"{name} background", dark == settings["night"], f"{hex_color(background)} {'dark' if dark else 'light'}")
    if heading:
        node = node_with_text(root, heading)
        if node is None:
            checks.check(f"{name} heading color", False, f"{heading!r} not in the dump")
        else:
            color, _ = screen.ink(bounds(node))
            accent = settings["accent"]
            # On a light page a pale accent is darkened for contrast (AppearanceColors.readable). zh-CN: 亮色页面上浅色强调色会加深.
            if color is None:
                ok = False
            elif settings["night"]:
                ok = close(color, accent, 40)
            else:
                ok = luminance(color) < luminance(accent) and (color >> 16 & 0xFF) - (color & 0xFF) > 20
            checks.check(f"{name} heading color", ok, f"text {hex_color(color or 0)}, accent {hex_color(accent)}")
    return root, screen


def paragraph_side(checks, name, root, screen, rtl):
    """A Latin library name sits at the start of the language: right in Arabic, left otherwise."""
    node = node_with_text(root, FIRST_LIBRARY)
    if node is None:
        checks.check(f"{name} paragraph direction", False, f"{FIRST_LIBRARY!r} not in the dump")
        return
    left, top, right, bottom = bounds(node)
    _, rows = screen.ink((left, top, right, bottom))
    if not rows:
        checks.check(f"{name} paragraph direction", False, "no text pixels")
        return
    low = min(x for x, _ in rows.values()) - left
    high = right - max(x for _, x in rows.values())
    width = right - left
    side = "right" if high < width * 0.1 and low > width * 0.3 else "left" if low < width * 0.1 and high > width * 0.3 else "unclear"
    checks.check(f"{name} paragraph direction", side == ("right" if rtl else "left"), f"{FIRST_LIBRARY!r} on the {side} (gap {low} / {high} px)")


def notice_left_to_right(device, checks, name, shots, screen):
    """Scrolls to the first NOTICE file: its short first line starts on the left even in Arabic."""
    for _ in range(10):
        device.swipe_up(screen)
        root = device.dump()
        bar = action_bar(root)
        floor = bounds(bar)[3] if bar is not None else 0
        node = next((n for n in root.iter("node") if (n.get("text") or "").startswith(NOTICE_FIRST_LINE) and chr(10) in (n.get("text") or "")), None)
        if node is None:
            continue
        left, top, right, bottom = bounds(node)
        if top <= floor + 4 or bottom - top < 80:
            continue
        screen = device.screen()
        if shots:
            screen.png(shots / f"{name}.png")
        _, rows = screen.ink((left + 4, top + 4, right - 4, bottom - 4))
        ink_rows = sorted(rows)
        if not ink_rows:
            break
        band = [ink_rows[0]]
        for y in ink_rows[1:]:
            if y != band[-1] + 1:
                break
            band.append(y)
        low = min(rows[y][0] for y in band) - left
        high = max(rows[y][1] for y in band) - left
        width = right - left
        ok = low < width * 0.15 and high < width * 0.7
        checks.check(f"{name} left to right", ok, f"first line from {low} to {high} px of {width}")
        return
    checks.check(f"{name} left to right", False, "the first NOTICE file did not come into view")


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("-s", "--serial", required=True)
    parser.add_argument("--apk", required=True, type=pathlib.Path, help="the debug APK of this plugin")
    parser.add_argument("--shots", type=pathlib.Path, help="directory for PNG screenshots of each page")
    args = parser.parse_args()
    sys.stdout.reconfigure(encoding="utf-8", errors="replace")
    device = Device(args.serial)
    checks = Checks()
    if args.shots:
        args.shots.mkdir(parents=True, exist_ok=True)

    package = device.shell(f"dumpsys package {HOST}")
    if "DEBUGGABLE" not in package:
        sys.exit(f"{HOST} must be a debuggable build on {args.serial}")
    if device.shell(f"run-as {HOST} ls shared_prefs", check=False).count(f"{HOST}_preferences.xml.bak"):
        sys.exit("the host preferences have a pending .bak file")
    plugin_before = PLUGIN in device.shell(f"pm list packages {PLUGIN}")
    device.stop_all()
    original = device.read_prefs()
    print(f"host preferences md5 {hashlib.md5(original).hexdigest()[:8]}, plugin installed before: {plugin_before}", flush=True)
    try:
        device.run("install", "-r", "-d", str(args.apk))

        # Cold start with the host in Arabic, dark, indigo; the host is stopped before every change of its preferences
        # (the install may have started it). zh-CN: 冷启动, 宿主为阿拉伯语, 深色, 靛蓝; 每次改宿主偏好前先停止宿主.
        device.shell(f"am force-stop {HOST}")
        device.write_prefs(with_host_settings(original, AR_DARK))
        device.start(SETTINGS)
        root, screen = page_appearance(device, checks, "settings-ar-dark", args.shots, ["المكتبات", FIRST_LIBRARY], AR_DARK, heading="المكتبات")
        paragraph_side(checks, "settings-ar-dark", root, screen, rtl=True)
        notice_left_to_right(device, checks, "settings-ar-dark-notice", args.shots, screen)

        device.start(RELEASE_HISTORY)
        page_appearance(device, checks, "release-history-ar-dark", args.shots, ["سجل الإصدارات", "1.0.0*"], AR_DARK)

        # Back to the page after a change of the host settings (onResume). zh-CN: 宿主设置改变后回到页面 (onResume).
        device.shell("am start -W -a android.intent.action.MAIN -c android.intent.category.HOME")
        device.shell(f"am force-stop {HOST}")
        device.write_prefs(with_host_settings(original, ZH_LIGHT))
        device.shell(f"am force-stop {PLUGIN}")
        device.start(SETTINGS)
        root, screen = page_appearance(device, checks, "settings-zh-light", args.shots, ["库", FIRST_LIBRARY, "在脚本中使用"], ZH_LIGHT, heading="库")
        paragraph_side(checks, "settings-zh-light", root, screen, rtl=False)

        device.start(RELEASE_HISTORY)
        page_appearance(device, checks, "release-history-zh-light", args.shots, ["发行历史", "1.0.0*"], ZH_LIGHT)

        # AutoJs6 disabled: the system appearance (light). zh-CN: 停用 AutoJs6: 系统外观 (亮色).
        device.shell(f"am force-stop {PLUGIN}")
        device.shell(f"pm disable-user --user 0 {HOST}")
        try:
            device.start(SETTINGS)
            page_appearance(device, checks, "settings-host-disabled", args.shots, [FIRST_LIBRARY], None)
            device.start(RELEASE_HISTORY)
            page_appearance(device, checks, "release-history-host-disabled", args.shots, ["1.0.0*"], None)
        finally:
            device.shell(f"pm enable {HOST}")
    finally:
        device.stop_all()
        device.write_prefs(original)
        restored = device.read_prefs()
        checks.check("restore host preferences", restored == original, f"md5 {hashlib.md5(restored).hexdigest()[:8]}")
        enabled = f"package:{HOST}" in device.shell(f"pm list packages -e {HOST}").split()
        checks.check("restore host enabled", enabled, "enabled" if enabled else "disabled")
        if not plugin_before:
            device.run("uninstall", PLUGIN, check=False)
    print("PASS" if not checks.failed else f"FAIL ({checks.failed})")
    return 1 if checks.failed else 0


if __name__ == "__main__":
    sys.exit(main())
