package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SleekBg
import com.example.ui.theme.SleekTextPrimary
import com.example.ui.theme.SleekTextSecondary
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.SleekCardGradStart
import com.example.ui.theme.SleekCardGradEnd
import com.example.ui.theme.SleekRewardBg
import com.example.ui.theme.SleekRewardBorder
import com.example.ui.theme.SleekIndicator
import com.example.ui.theme.PureWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("ai_milestone_screen")
                ) { innerPadding ->
                    MilestoneDashboard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MilestoneDashboard(modifier: Modifier = Modifier) {
    var currentDay by remember { mutableStateOf(1) }
    var showCelebrationDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Smooth progress indicator animation matching the active milestone day
    val targetProgress = if (currentDay == 1) 0.2f else 0.4f
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 800),
        label = "progressAnimation"
    )

    Box(
        modifier = modifier
            .background(SleekBg)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Header Section
            HeaderSection()

            // 2. Milestone & Progress Card (Visual Focus)
            MilestoneProgressCard(
                progress = animatedProgress,
                currentDay = currentDay,
                onCardClick = {
                    showCelebrationDialog = true
                }
            )

            // 3. Rewards Section
            RewardsSection(isDay2Active = currentDay == 2)

            // Space to push the footer elegantly
            Spacer(modifier = Modifier.weight(1f, fill = false))

            // 4. Encouragement Footer & Action Button
            FooterSection(
                currentDay = currentDay,
                onBeginDay2Click = {
                    if (currentDay == 1) {
                        currentDay = 2
                        showCelebrationDialog = true
                    }
                }
            )
        }

        // Celebration dialog
        if (showCelebrationDialog) {
            CelebrationDialog(
                currentDay = currentDay,
                onDismiss = { showCelebrationDialog = false }
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Personalized Headline with purple highlighted "Upasana"
        Text(
            text = buildAnnotatedString {
                append("Hello ")
                withStyle(style = SpanStyle(color = SleekPurple, fontWeight = FontWeight.Bold)) {
                    append("Upasana")
                }
                append("! Welcome to the world of AI.")
            },
            style = MaterialTheme.typography.headlineLarge,
            color = SleekTextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = (-0.5).sp
        )

        // Supportive sub-headline
        Text(
            text = buildAnnotatedString {
                append("Step into the bold new era of ")
                withStyle(style = SpanStyle(color = SleekPurple, fontWeight = FontWeight.Medium)) {
                    append("Agentic Engineering")
                }
                append(".")
            },
            style = MaterialTheme.typography.bodyMedium,
            color = SleekTextSecondary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun MilestoneProgressCard(
    progress: Float,
    currentDay: Int,
    onCardClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("milestone_card")
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(32.dp),
                clip = false,
                ambientColor = SleekPurple.copy(alpha = 0.2f),
                spotColor = SleekPurple.copy(alpha = 0.3f)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(SleekCardGradStart, SleekCardGradEnd)
                ),
                shape = RoundedCornerShape(32.dp)
            )
            .clickable(onClick = onCardClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Trophy Badge Visual
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .shadow(elevation = 8.dp, shape = CircleShape)
                    .background(PureWhite, CircleShape)
                    .border(4.dp, SleekPurple, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🏆",
                        fontSize = 36.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "ELITE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekPurple,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Milestone Text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (currentDay == 1) "Day 1 Complete" else "Day 2 Active",
                    style = MaterialTheme.typography.headlineSmall,
                    color = SleekPurpleDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Milestone $currentDay of 5 achieved",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SleekTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Interactive Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Official Start",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekPurple,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${(progress * 100).toInt()}% Progress",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekPurple,
                        letterSpacing = 0.5.sp
                    )
                }

                // Custom Rounded Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .background(PureWhite.copy(alpha = 0.5f), CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .shadow(
                                elevation = 4.dp,
                                shape = CircleShape,
                                clip = false,
                                spotColor = SleekPurple.copy(alpha = 0.4f)
                            )
                            .background(SleekPurple, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun RewardsSection(isDay2Active: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("rewards_container"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "REWARDS UNLOCKED",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = SleekTextSecondary,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
        )

        // Grid-like dynamic rewards display using rows for precise layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RewardItemCard(
                title = "Mission Control Authorized",
                icon = Icons.Filled.Terminal,
                modifier = Modifier.weight(1f)
            )
            RewardItemCard(
                title = "Cloud Run Bridge Active",
                icon = Icons.Filled.CloudQueue,
                modifier = Modifier.weight(1f)
            )
        }

        // Additional reward animates in when Day 2 is unlocked!
        AnimatedVisibility(
            visible = isDay2Active,
            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                initialOffsetY = { 20 },
                animationSpec = tween(500)
            )
        ) {
            RewardItemCard(
                title = "Architecture Blueprint Configured",
                icon = Icons.Filled.Hub,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RewardItemCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SleekRewardBg),
        modifier = modifier
            .border(1.dp, SleekRewardBorder, RoundedCornerShape(16.dp))
            .clickable { /* Active micro-interaction ripple */ }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(SleekPurple, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PureWhite,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = SleekPurpleDark,
                lineHeight = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FooterSection(
    currentDay: Int,
    onBeginDay2Click: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encouragement quote text
        Text(
            text = "\"Learn, Iterate, and Grow.\"",
            style = MaterialTheme.typography.bodyMedium,
            color = SleekTextSecondary,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Sleek Fully-Rounded Button
        Button(
            onClick = onBeginDay2Click,
            colors = ButtonDefaults.buttonColors(
                containerColor = SleekPurple,
                contentColor = PureWhite,
                disabledContainerColor = SleekRewardBorder
            ),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("begin_day_2_button")
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(28.dp),
                    clip = false,
                    ambientColor = SleekPurple.copy(alpha = 0.25f),
                    spotColor = SleekPurple.copy(alpha = 0.4f)
                ),
            shape = RoundedCornerShape(28.dp),
            enabled = currentDay == 1
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (currentDay == 1) "Begin Day 2: Architecture Setup" else "Day 2 Initiated!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Interactive Home Gesture Indicator Bar
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(128.dp)
                .height(6.dp)
                .background(SleekIndicator, CircleShape)
        )
    }
}

@Composable
fun CelebrationDialog(currentDay: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SleekPurple)
            ) {
                Text(
                    text = "Let's Go!",
                    color = PureWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Celebration,
                    contentDescription = "Celebration",
                    tint = SleekPurple,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = if (currentDay == 1) "Day 1 Mastered!" else "Day 2 Active!",
                    color = SleekPurpleDark,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Text(
                text = if (currentDay == 1) {
                    "Superb effort, Upasana! Day 1 is successfully verified. Tap to begin Day 2!"
                } else {
                    "Day 2 Architecture Setup active! The third reward is officially unlocked, preparing you with clean architectural blueprints."
                },
                color = SleekTextSecondary,
                lineHeight = 20.sp
            )
        },
        containerColor = PureWhite,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.border(1.dp, SleekRewardBorder, RoundedCornerShape(24.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun MilestoneDashboardPreview() {
    MyApplicationTheme {
        MilestoneDashboard()
    }
}
