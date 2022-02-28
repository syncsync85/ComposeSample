package com.sync.composesampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.sync.composesampleapp.ui.theme.ComposeSampleAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleAppTheme() {
                SampleApplication()
            }
        }
    }
}

@Composable
fun SampleApplication(contactProfiles: List<ContactProfile> = userProfileList) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "users_list") {
        composable("users_list") {
            UserListScreen(contactProfiles, navController)
        }
        composable(
            route = "user_details/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"), navController)
        }
    }
}

@Composable
fun UserListScreen(contactProfiles: List<ContactProfile>, navController: NavHostController?) {
    Scaffold(topBar = {
        AppBar(
            title = "Contacts",
            icon = Icons.Default.Home
        ) { }
    }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn {
                items(contactProfiles) { userProfile ->
                    ProfileCard(contactProfile = userProfile) {
                        navController?.navigate("user_details/${userProfile.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavHostController?) {
    val userProfile = userProfileList.first { userProfile -> userId == userProfile.id }
    Scaffold(topBar = {
        AppBar(
            title = "Contact Details",
            icon = Icons.Default.ArrowBack
        ) {
            navController?.navigateUp()
        }
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                DetailProfilePicture(userProfile.desc, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
                ProfileDesc(userProfile.desc, userProfile.status, Alignment.CenterHorizontally)
            }
        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable(onClick = { iconClickAction.invoke() })
            )
        },
        title = { Text(title) }
    )
}

@Composable
fun ProfileCard(contactProfile: ContactProfile, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable(onClick = { clickAction.invoke() }),
        elevation = 4.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(contactProfile.desc, contactProfile.status, 72.dp)
            ProfileContent(contactProfile.name, contactProfile.status, Alignment.Start)
        }

    }
}

@Composable
fun ProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = Color.DarkGray
        ),
        modifier = Modifier
            .padding(16.dp)
            .size(imageSize),
        elevation = 2.dp
    ) {
        Image(
            painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier.size(72.dp),
            contentDescription = "Profile picture description",
        )
    }
}

@Composable
fun DetailProfilePicture(pictureUrl: String, onlineStatus: Boolean, imageSize: Dp) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(2.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            border = BorderStroke(
                width = 1.dp,
                color = Color.DarkGray
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(imageSize),
            elevation = 2.dp
        ) {
            Image(
                painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier.size(72.dp),
                contentDescription = "Profile picture description",
                alignment = Alignment.Center
            )
        }
    }

}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.h5
        )
        Text(
            text = if (onlineStatus)
                "Active now"
            else "Offline",
            style = MaterialTheme.typography.body2
        )
    }

}

@Composable
fun ProfileDesc(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        Text(
            text = userName,
            style = MaterialTheme.typography.subtitle1
        )
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun ContactListPreview() {
    ComposeSampleAppTheme {
        UserListScreen(contactProfiles = userProfileList, null)
    }
}

@Preview(showBackground = true)
@Composable
fun ContactDetailPreview() {
    ComposeSampleAppTheme {
        UserProfileDetailsScreen(userId = 0, null)
    }
}