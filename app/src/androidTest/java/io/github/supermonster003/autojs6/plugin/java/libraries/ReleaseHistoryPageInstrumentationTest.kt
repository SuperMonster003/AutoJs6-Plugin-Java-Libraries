package io.github.supermonster003.autojs6.plugin.java.libraries

import android.app.Activity
import android.content.Intent
import android.os.ParcelFileDescriptor
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeTimeoutException
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The release history page (AUTOJS6_PLUGIN_RELEASE_HISTORY_AGENTS.md): its title is "Release history" with the plugin
 * name as the subtitle, the newest release starts expanded, the settings row opens it, and the keyboard reaches every
 * release. The keyboard test waits for window focus before every key (plugin-center Q137).
 * zh-CN: 发行历史页: 标题为 "发行历史", 副标题为插件名, 最新版默认展开, 设置页的行可打开它, 键盘能到达每个版本.
 * 键盘用例每次按键前等待窗口焦点 (plugin-center Q137).
 */
@RunWith(AndroidJUnit4::class)
class ReleaseHistoryPageInstrumentationTest {

    @get:Rule
    val compose = createEmptyComposeRule()

    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val context get() = instrumentation.targetContext

    @After fun finishActivities() {
        instrumentation.setInTouchMode(true)
        instrumentation.runOnMainSync {
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).forEach(Activity::finish)
        }
        instrumentation.waitForIdleSync()
    }

    /** Unmerged: the count chips sit inside the clickable header, which merges its children. */
    private fun exists(tag: String): Boolean =
        runCatching { compose.onAllNodes(hasTestTag(tag), useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty() }.getOrDefault(false)

    private fun resumed(): Activity? {
        var activity: Activity? = null
        instrumentation.runOnMainSync {
            activity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).firstOrNull()
        }
        return activity
    }

    private fun assertTitleAndSubtitle() {
        val inTopBar = hasAnyAncestor(hasTestTag("$TAG.topBar"))
        compose.onNode(hasText(context.getString(R.string.text_release_history)) and inTopBar).assertIsDisplayed()
        compose.onNode(hasText(context.getString(R.string.app_name)) and inTopBar).assertIsDisplayed()
    }

    @Test fun theTitleIsReleaseHistoryAndTheSubtitleThePluginName() {
        ActivityScenario.launch(ReleaseHistoryActivity::class.java).use {
            compose.waitUntil(TIMEOUT) { exists("$TAG.releaseHistory.list") }
            assertTitleAndSubtitle()
            assertFalse(exists("$TAG.releaseHistory.empty"))
        }
    }

    private fun versions(scenario: ActivityScenario<ReleaseHistoryActivity>): List<String> {
        var versions = emptyList<String>()
        scenario.onActivity { activity ->
            val candidates = ReleaseHistoryAssetPolicy.candidatesFor(activity.resources.configuration.locales[0])
            versions = loadReleaseHistory(candidates) { path ->
                activity.assets.open(path).use { it.readBytes().toString(Charsets.UTF_8) }
            }.map { it.version }
        }
        return versions
    }

    /**
     * The newest release starts expanded, the others show their category counts; a header expands or collapses its
     * release, the top bar expands or collapses all of them, and the choice survives recreation.
     */
    @Test fun releasesExpandAndCollapse() {
        ActivityScenario.launch(ReleaseHistoryActivity::class.java).use { scenario ->
            compose.waitUntil(TIMEOUT) { exists("$TAG.releaseHistory.list") }
            val versions = versions(scenario)
            assertTrue("releases: ${versions.size}", versions.size >= 2)
            val (newest, second) = versions
            assertTrue(exists("$TAG.releaseHistory.body.$newest"))
            assertFalse(exists("$TAG.releaseHistory.counts.$newest"))
            assertFalse(exists("$TAG.releaseHistory.body.$second"))
            assertTrue(exists("$TAG.releaseHistory.counts.$second"))

            // The expanded newest release can push the second header below the screen (phones, large fonts).
            compose.onNodeWithTag("$TAG.releaseHistory.entry.$second").performScrollTo().performClick()
            compose.waitUntil(TIMEOUT) { exists("$TAG.releaseHistory.body.$second") }
            compose.onNodeWithTag("$TAG.releaseHistory.entry.$newest").performScrollTo().performClick()
            compose.waitUntil(TIMEOUT) { !exists("$TAG.releaseHistory.body.$newest") && exists("$TAG.releaseHistory.counts.$newest") }

            scenario.recreate()
            compose.waitUntil(TIMEOUT) { exists("$TAG.releaseHistory.list") }
            assertTrue(exists("$TAG.releaseHistory.body.$second"))
            assertFalse(exists("$TAG.releaseHistory.body.$newest"))

            compose.onNodeWithTag("$TAG.releaseHistory.toggleAll")
                .assertContentDescriptionEquals(context.getString(R.string.text_release_history_expand_all))
                .performClick()
            compose.waitUntil(TIMEOUT) { exists("$TAG.releaseHistory.body.$newest") }
            compose.onNodeWithTag("$TAG.releaseHistory.toggleAll")
                .assertContentDescriptionEquals(context.getString(R.string.text_release_history_collapse_all))
                .performClick()
            compose.waitUntil(TIMEOUT) { !exists("$TAG.releaseHistory.body.$newest") && !exists("$TAG.releaseHistory.body.$second") }
        }
    }

    private fun View.findTagged(tag: Any): View? {
        if (this.tag == tag) return this
        if (this is ViewGroup) (0 until childCount).forEach { index -> getChildAt(index).findTagged(tag)?.let { return it } }
        return null
    }

    @Test fun theSettingsRowOpensThePage() {
        val page = instrumentation.startActivitySync(
            Intent(context, SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
        instrumentation.runOnMainSync {
            val row = requireNotNull(page.window.decorView.findTagged(R.string.text_release_history)) { "no release history row" }
            row.performClick()
        }
        compose.waitUntil(TIMEOUT) { resumed() is ReleaseHistoryActivity && exists("$TAG.releaseHistory.list") }
        assertTitleAndSubtitle()
    }

    private fun key(code: Int) {
        instrumentation.sendKeyDownUpSync(code)
        compose.waitForIdle()
    }

    /**
     * Leaves touch mode as a keyboard user does. Newer versions take [android.app.Instrumentation.setInTouchMode] at
     * once; older ones (API 24) learn it only from a key, so an up arrow goes first.
     */
    private fun leaveTouchMode() {
        instrumentation.setInTouchMode(false)
        key(KeyEvent.KEYCODE_DPAD_UP)
    }

    /** The window with input focus as `dumpsys window` reports it; for failure messages only (it connects UiAutomation). */
    private fun focusedWindow(): String = runCatching {
        ParcelFileDescriptor.AutoCloseInputStream(instrumentation.uiAutomation.executeShellCommand("dumpsys window windows"))
            .bufferedReader().useLines { lines -> lines.map(String::trim).filter { it.startsWith("mCurrentFocus=") }.distinct().joinToString() }
            .ifEmpty { "mCurrentFocus not in dumpsys" }
    }.getOrElse { "dumpsys failed: $it" }

    /** Keys go to the focused window: while a system dialog (an ANR, say) has focus, every Tab would be lost. */
    private fun awaitWindowFocus(scenario: ActivityScenario<*>, visited: List<String> = emptyList()) {
        var focused = false
        try {
            compose.waitUntil(TIMEOUT) { scenario.onActivity { focused = it.hasWindowFocus() }; focused }
        } catch (timeout: ComposeTimeoutException) {
            fail("the page has no window focus after $TIMEOUT ms; visited: $visited; ${focusedWindow()}")
        }
    }

    /**
     * The test tag of the focused control (or of its nearest tagged ancestor), null when nothing has focus. The back
     * button has no tag of its own, so inside the top bar it is told apart by its content description.
     */
    private fun focusedControl(): String? {
        val node = compose.onAllNodes(isFocused(), useUnmergedTree = true).fetchSemanticsNodes().firstOrNull() ?: return null
        val tag = generateSequence(node) { it.parent }.mapNotNull { it.config.getOrNull(SemanticsProperties.TestTag) }.firstOrNull()
        if (tag == "$TAG.topBar") {
            val description = compose.onNode(isFocused()).fetchSemanticsNode().config.getOrNull(SemanticsProperties.ContentDescription)
            if (description == listOf(context.getString(R.string.text_back))) return BACK
        }
        return tag ?: UNTAGGED
    }

    /** Presses Tab until focus returns to a control seen before; every focused control must be on screen. */
    private fun tabCycle(scenario: ActivityScenario<*>): List<String> {
        val visited = mutableListOf<String>()
        repeat(MAX_TABS) {
            awaitWindowFocus(scenario, visited)
            key(KeyEvent.KEYCODE_TAB)
            val control = focusedControl() ?: return@repeat
            if (control in visited) return visited
            compose.onNode(isFocused(), useUnmergedTree = true).assertIsDisplayed()
            visited += control
        }
        return visited
    }

    /**
     * Keyboard users read the whole page (AUTOJS6_PLUGIN_RELEASE_HISTORY_AGENTS.md section 10): Tab passes the back
     * button and then every version header in order, down to the oldest release.
     */
    @Test fun theReleaseHistoryCanBeReadToTheEndFromTheKeyboard() {
        ActivityScenario.launch(ReleaseHistoryActivity::class.java).use { scenario ->
            compose.waitUntil(TIMEOUT) { exists("$TAG.releaseHistory.list") }
            val versions = versions(scenario)
            assertTrue("releases: ${versions.size}", versions.isNotEmpty())
            awaitWindowFocus(scenario)
            leaveTouchMode()
            val visited = tabCycle(scenario)
            fun expect(ok: Boolean, what: String) {
                if (!ok) fail("$what; visited: $visited; ${focusedWindow()}")
            }
            expect(BACK in visited, "Tab never reached the back button")
            expect(UNTAGGED !in visited, "something focusable has no name")
            val expected = versions.map { "$TAG.releaseHistory.entry.$it" }
            val entries = visited.filter { it.startsWith("$TAG.releaseHistory.entry.") }
            expect(entries == expected, "the releases in Tab order: expected $expected, got $entries")
        }
    }

    private companion object {
        const val TAG = "javaLibraries"
        const val TIMEOUT = 15_000L
        const val MAX_TABS = 120
        const val UNTAGGED = "(untagged)"
        const val BACK = "(back)"
    }
}
