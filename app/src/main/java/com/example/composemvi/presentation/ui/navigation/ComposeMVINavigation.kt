import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailScreen
import com.example.composemvi.presentation.ui.feature.bookdetail.BookDetailViewModel
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookScreen
import com.example.composemvi.presentation.ui.feature.bookmark.BookmarkedBookViewModel
import com.example.composemvi.presentation.ui.feature.booksearch.BookSearchScreen
import com.example.composemvi.presentation.ui.feature.booksearch.BookSearchViewModel
import com.example.composemvi.presentation.ui.navigation.NavigationArgument.BOOKMARKED_BOOKS
import com.example.composemvi.presentation.ui.navigation.NavigationArgument.BOOK_DETAIL_ROUTE
import com.example.composemvi.presentation.ui.navigation.NavigationArgument.BOOK_ISBN
import com.example.composemvi.presentation.ui.navigation.NavigationArgument.BOOK_SEARCH

@Composable
fun ComposeMVINavigation(navController: NavHostController) {
    val snackbarState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                BookSearchNavigationItem(navController)
                BookmarkNavigationItem(navController)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
    ) { innerPadding -> ComposeMVINavHost(navController, innerPadding, snackbarState) }
}

@Composable
fun ComposeMVINavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    snackbarState: SnackbarHostState,
) {
    val bookSearchViewModel: BookSearchViewModel = hiltViewModel()
    val bookmarkedBookViewModel: BookmarkedBookViewModel = hiltViewModel()
    val bookDetailViewModel: BookDetailViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = BOOK_SEARCH,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(BOOK_SEARCH) {
            BookSearchScreen(
                navController = navController,
                viewModel = bookSearchViewModel,
                snackbarState = snackbarState,
            )
        }

        composable(BOOKMARKED_BOOKS) {
            BookmarkedBookScreen(
                navController = navController,
                viewModel = bookmarkedBookViewModel,
                snackbarState = snackbarState,
            )
        }

        composable(
            route = BOOK_DETAIL_ROUTE,
            arguments = listOf(navArgument(BOOK_ISBN) { type = NavType.StringType }),
        ) { backStackEntry ->
            val isbn = backStackEntry.arguments?.getString(BOOK_ISBN)
            BookDetailScreen(
                isbn = isbn,
                snackbarState = snackbarState,
                viewModel = bookDetailViewModel,
            )
        }
    }
}

@Composable
private fun RowScope.BookSearchNavigationItem(navController: NavHostController) {
    NavigationBarItem(
        icon = {
            Icon(
                Icons.Default.Search,
                contentDescription = BOOK_SEARCH,
            )
        },
        selected = navController.currentDestination?.route == BOOK_SEARCH,
        onClick = { navController.navigate(route = BOOK_SEARCH) },
    )
}

@Composable
private fun RowScope.BookmarkNavigationItem(navController: NavHostController) {
    NavigationBarItem(
        icon = {
            Icon(
                Icons.Default.BookmarkBorder,
                contentDescription = BOOKMARKED_BOOKS,
            )
        },
        selected = navController.currentDestination?.route == BOOKMARKED_BOOKS,
        onClick = { navController.navigate(route = BOOKMARKED_BOOKS) },
    )
}
