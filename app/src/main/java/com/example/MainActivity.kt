package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.draw.alpha
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
import com.example.ui.theme.TokyoBg
import com.example.ui.theme.TokyoCard
import com.example.ui.theme.TokyoTextPrimary
import com.example.ui.theme.TokyoTextSecondary
import com.example.ui.theme.TokyoBlue
import com.example.ui.theme.TokyoTeal
import com.example.ui.theme.TokyoPurple
import com.example.ui.theme.TokyoOrange
import com.example.ui.theme.TokyoBorder
import com.example.ui.theme.TokyoTextBright
import kotlinx.coroutines.delay

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
    var activeDay by remember { mutableStateOf(2) } // Selected dashboard view: 2, 3, or 4
    var day3Completed by remember { mutableStateOf(false) }
    var day3Loading by remember { mutableStateOf(false) }
    var day4Triggered by remember { mutableStateOf(false) }
    var showToastMessage by remember { mutableStateOf<String?>(null) }
    var showDialogMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    // Smooth progress indicator animation matching the track completion
    val targetProgress = when {
        day4Triggered -> 0.8f
        day3Completed -> 0.6f
        else -> 0.4f
    }
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 800),
        label = "progressAnimation"
    )

    // Simulate diagnostic loading
    LaunchedEffect(day3Loading) {
        if (day3Loading) {
            delay(2500)
            day3Loading = false
            day3Completed = true
            showToastMessage = "🎉 Day 3 Completed: Google-Agents-CLI & Cloud Run Architecture Loaded!"
        }
    }

    // Auto-dismiss custom toast after 4 seconds
    LaunchedEffect(showToastMessage) {
        if (showToastMessage != null) {
            delay(4000)
            showToastMessage = null
        }
    }

    Box(
        modifier = modifier
            .background(TokyoBg)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Header Section
            HeaderSection()

            // 2. Retro-Futuristic Course Dashboard Selector Tab Row
            ProgressTrackerTabs(
                activeDay = activeDay,
                day3Completed = day3Completed,
                day3Loading = day3Loading,
                onSelectDay = { day ->
                    if (day == 3) {
                        if (!day3Completed && !day3Loading) {
                            day3Loading = true
                        }
                        activeDay = 3
                    } else if (day == 4) {
                        if (day3Completed) {
                            activeDay = 4
                            day4Triggered = true
                            showDialogMessage = "🚀 Initializing Day 4: Live Cloud Deployment Phase Triggered."
                        }
                    } else {
                        activeDay = 2
                    }
                }
            )

            // 3. Dynamic Visual Milestone Progress Card
            InteractiveProgressCard(
                progress = animatedProgress,
                activeDay = activeDay,
                day3Completed = day3Completed,
                day3Loading = day3Loading,
                day4Triggered = day4Triggered
            )

            // 4. Milestone Task List for selected Active Day
            DynamicMilestonesList(
                activeDay = activeDay,
                day3Loading = day3Loading,
                day3Completed = day3Completed,
                day4Triggered = day4Triggered,
                onStartDiagnostics = {
                    if (!day3Completed && !day3Loading) {
                        day3Loading = true
                    }
                }
            )

            // Space to push footer elegantly
            Spacer(modifier = Modifier.weight(1f, fill = false))

            // 5. Encouragement Footer & Action Button
            FooterActionSection(
                activeDay = activeDay,
                day3Completed = day3Completed,
                day3Loading = day3Loading,
                onActionButtonClick = {
                    if (activeDay == 2) {
                        activeDay = 3
                        if (!day3Completed && !day3Loading) {
                            day3Loading = true
                        }
                    } else if (activeDay == 3 && day3Completed) {
                        activeDay = 4
                        day4Triggered = true
                        showDialogMessage = "🚀 Initializing Day 4: Live Cloud Deployment Phase Triggered."
                    }
                }
            )
        }

        // Custom High-Polish Toast Banner
        AnimatedVisibility(
            visible = showToastMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -40 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -40 }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TokyoCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, TokyoTeal, RoundedCornerShape(16.dp))
                    .shadow(12.dp, shape = RoundedCornerShape(16.dp), spotColor = TokyoTeal)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(TokyoTeal.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Celebration,
                            contentDescription = "Success Icon",
                            tint = TokyoTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = showToastMessage ?: "",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TokyoTextBright,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Custom Modal / Alert Dialog for Day 4 Trigger
        if (showDialogMessage != null) {
            AlertDialog(
                onDismissRequest = { showDialogMessage = null },
                confirmButton = {
                    Button(
                        onClick = { showDialogMessage = null },
                        colors = ButtonDefaults.buttonColors(containerColor = TokyoBlue)
                    ) {
                        Text("Launch Build!", color = TokyoBg, fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudQueue,
                            contentDescription = "Cloud Icon",
                            tint = TokyoBlue,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Phase Unlocked!",
                            color = TokyoTextBright,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Text(
                        text = showDialogMessage ?: "",
                        color = TokyoTextPrimary,
                        lineHeight = 20.sp
                    )
                },
                containerColor = TokyoCard,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.border(1.dp, TokyoBorder, RoundedCornerShape(24.dp))
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(TokyoTeal, CircleShape)
            )
            Text(
                text = "TRAINING TRACK ACTIVE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TokyoTeal,
                letterSpacing = 1.5.sp
            )
        }
        Text(
            text = buildAnnotatedString {
                append("Hello ")
                withStyle(style = SpanStyle(color = TokyoPurple, fontWeight = FontWeight.Bold)) {
                    append("Upasana")
                }
                append("! Welcome to the world of AI.")
            },
            style = MaterialTheme.typography.headlineMedium,
            color = TokyoTextBright,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = buildAnnotatedString {
                append("Step into the bold new era of ")
                withStyle(style = SpanStyle(color = TokyoBlue, fontWeight = FontWeight.SemiBold)) {
                    append("Agentic Engineering")
                }
                append(".")
            },
            style = MaterialTheme.typography.bodyMedium,
            color = TokyoTextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun ProgressTrackerTabs(
    activeDay: Int,
    day3Completed: Boolean,
    day3Loading: Boolean,
    onSelectDay: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "COURSE DASHBOARD & TRACKER",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TokyoTextSecondary,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(TokyoCard.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .border(1.dp, TokyoBorder, RoundedCornerShape(16.dp))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Day 2 Tab
            val day2Active = activeDay == 2
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (day2Active) TokyoCard else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (day2Active) TokyoTeal.copy(alpha = 0.3f) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelectDay(2) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Day 2",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (day2Active) TokyoTeal else TokyoTextPrimary
                    )
                    Text(
                        text = "✓ Done",
                        fontSize = 9.sp,
                        color = TokyoTeal,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Day 3 Tab
            val day3Active = activeDay == 3
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (day3Active) TokyoCard else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (day3Active) (if (day3Completed) TokyoTeal.copy(alpha = 0.3f) else TokyoOrange.copy(alpha = 0.3f)) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelectDay(3) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Day 3",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (day3Active) (if (day3Completed) TokyoTeal else TokyoOrange) else TokyoTextPrimary
                    )
                    Text(
                        text = when {
                            day3Loading -> "⚡ Diagnosing"
                            day3Completed -> "✓ Loaded"
                            else -> "⚡ Start"
                        },
                        fontSize = 9.sp,
                        color = if (day3Completed) TokyoTeal else TokyoOrange,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Day 4 Tab
            val day4Active = activeDay == 4
            val day4Enabled = day3Completed
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (day4Active) TokyoCard else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (day4Active) TokyoBlue.copy(alpha = 0.3f) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(enabled = day4Enabled) { onSelectDay(4) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.alpha(if (day4Enabled) 1f else 0.4f)
                ) {
                    Text(
                        text = "Day 4",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (day4Active) TokyoBlue else (if (day4Enabled) TokyoTextPrimary else TokyoTextSecondary)
                    )
                    Text(
                        text = if (day4Enabled) "🚀 Ready" else "🔒 Locked",
                        fontSize = 9.sp,
                        color = if (day4Enabled) TokyoBlue else TokyoTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveProgressCard(
    progress: Float,
    activeDay: Int,
    day3Completed: Boolean,
    day3Loading: Boolean,
    day4Triggered: Boolean
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = TokyoCard),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("milestone_card")
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false,
                ambientColor = TokyoBlue.copy(alpha = 0.1f),
                spotColor = TokyoPurple.copy(alpha = 0.2f)
            )
            .border(1.dp, TokyoBorder, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main Glowing Circle with Adaptive Emoji
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .shadow(elevation = 6.dp, shape = CircleShape)
                    .background(TokyoBg, CircleShape)
                    .border(
                        width = 2.dp,
                        color = when {
                            day4Triggered -> TokyoBlue
                            day3Completed -> TokyoTeal
                            day3Loading -> TokyoOrange
                            else -> TokyoPurple
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        day4Triggered -> "🚀"
                        day3Completed -> "🏆"
                        day3Loading -> "⚡"
                        else -> "🛸"
                    },
                    fontSize = 38.sp
                )
            }

            // Central status text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = when {
                        day4Triggered -> "Day 4 Initiated"
                        day3Completed -> "Day 3 Completed"
                        day3Loading -> "Day 3 Diagnosing..."
                        else -> "Day 2 Completed"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = TokyoTextBright,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track Progress: ${(progress * 100).toInt()}% Achieved",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TokyoTextPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            // Glow Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TRACK PROGRESS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TokyoTextSecondary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TokyoTeal,
                        letterSpacing = 1.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(TokyoBg, CircleShape)
                        .border(1.dp, TokyoBorder, CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(TokyoBlue, TokyoPurple)
                                ),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun DynamicMilestonesList(
    activeDay: Int,
    day3Loading: Boolean,
    day3Completed: Boolean,
    day4Triggered: Boolean,
    onStartDiagnostics: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = when (activeDay) {
                2 -> "DAY 2 COMPLETED MILESTONES"
                3 -> if (day3Completed) "DAY 3 LOADED ARCHITECTURE" else "DAY 3 REQUIRED DIAGNOSTICS"
                else -> "DAY 4 LIVE DEPLOYMENT TARGETS"
            },
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TokyoTextSecondary,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(start = 4.dp)
        )

        if (activeDay == 3 && day3Loading) {
            // Elegant Loading State Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TokyoCard.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TokyoOrange.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = TokyoOrange,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )
                    Text(
                        text = "Running Day 3 Agent Cloud Diagnostics...",
                        color = TokyoOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Analyzing Google-Agents-CLI & compiling environment manifests...",
                        color = TokyoTextPrimary,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                }
            }
        } else {
            val milestones = when (activeDay) {
                2 -> listOf(
                    "Dynamic Flask Application Build",
                    "Custom Dockerfile Architecture Set Up",
                    "Model Context Protocol (MCP) Server Integrated"
                )
                3 -> listOf(
                    "Google-Agents-CLI Configured",
                    "Cloud Run Target Environment Initialized",
                    "Docker Push Integration Verified"
                )
                else -> listOf(
                    "Live URL Endpoint Configured",
                    "Continuous Integration Pipeline Active",
                    "Production Monitoring Operational"
                )
            }

            milestones.forEachIndexed { index, milestone ->
                val badgeColor = when (activeDay) {
                    2 -> TokyoTeal
                    3 -> if (day3Completed) TokyoTeal else TokyoOrange
                    else -> TokyoBlue
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TokyoCard.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TokyoBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(TokyoBg, RoundedCornerShape(10.dp))
                                .border(1.dp, badgeColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "0${index + 1}",
                                fontWeight = FontWeight.Bold,
                                color = badgeColor,
                                fontSize = 13.sp
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = milestone,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TokyoTextBright,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = when (activeDay) {
                                    2 -> "Jinja2 Template Rendered • Local Verified"
                                    3 -> "Google Agents CLI System • Ready"
                                    else -> "GCP Cloud Target • Ready"
                                },
                                fontSize = 11.sp,
                                color = TokyoTextPrimary
                            )
                        }
                    }
                }
            }

            if (activeDay == 3 && !day3Completed) {
                // Diagnose CTA for Day 3
                Button(
                    onClick = onStartDiagnostics,
                    colors = ButtonDefaults.buttonColors(containerColor = TokyoOrange),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Run Day 3 Agent Diagnostics", color = TokyoBg, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FooterActionSection(
    activeDay: Int,
    day3Completed: Boolean,
    day3Loading: Boolean,
    onActionButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = TokyoCard.copy(alpha = 0.3f)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TokyoBorder.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\"Learn, Iterate, and Grow.\"",
                    color = TokyoTextPrimary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
                )
            }
        }

        val buttonEnabled = when (activeDay) {
            2 -> true
            3 -> day3Completed
            else -> false
        }

        val buttonColor = when (activeDay) {
            2 -> TokyoPurple
            3 -> TokyoBlue
            else -> TokyoTextSecondary
        }

        val buttonText = when (activeDay) {
            2 -> "Proceed to Day 3 Setup"
            3 -> if (day3Completed) "Advance to Day 4" else "Day 3 Diagnostics Required"
            else -> "Live Cloud Deployment Phase Triggered"
        }

        Button(
            onClick = onActionButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = TokyoBg,
                disabledContainerColor = TokyoBorder
            ),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("begin_day_2_button")
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = false,
                    ambientColor = buttonColor.copy(alpha = 0.2f),
                    spotColor = buttonColor.copy(alpha = 0.3f)
                ),
            shape = RoundedCornerShape(16.dp),
            enabled = buttonEnabled
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (buttonEnabled) TokyoBg else TokyoTextSecondary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (buttonEnabled) TokyoBg else TokyoTextSecondary
                )
            }
        }

        // Modern Home Gesture Bar Decorator
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(112.dp)
                .height(5.dp)
                .background(TokyoBorder, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MilestoneDashboardPreview() {
    MyApplicationTheme {
        MilestoneDashboard()
    }
}
