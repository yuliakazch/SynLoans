package com.yuliakazachok.synloans.desktop

import com.yuliakazachok.synloans.desktop.navigation.NavigationTree
import com.yuliakazachok.synloans.desktop.navigation.buildComposeNavigationGraph
import com.yuliakazachok.synloans.di.initKoin
import ru.alexgladkov.odyssey.compose.DesktopScreenHost
import ru.alexgladkov.odyssey.compose.extensions.setupWithRootController
import javax.swing.JFrame
import javax.swing.SwingUtilities

fun main() = SwingUtilities.invokeLater {
    val window = JFrame()
    window.title = "Syndicated Loans"
    window.setSize(1200, 850)

    val koin = initKoin().koin

    DesktopScreenHost(window)
        .setupWithRootController(
            startScreen = NavigationTree.Root.SignIn.name,
            block = buildComposeNavigationGraph(koin)
        )
}