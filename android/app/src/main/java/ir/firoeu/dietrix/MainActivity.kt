@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package ir.firoeu.dietrix

import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DietrixApp() }
    }
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF6A4BD4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE7DEFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF1F6FC2),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD3E4FF),
    onSecondaryContainer = Color(0xFF001C38),
    tertiary = Color(0xFF2F9464),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB5F1CE),
    onTertiaryContainer = Color(0xFF00210F),
    background = Color(0xFFF3F0FB),
    onBackground = Color(0xFF23222B),
    surface = Color(0xFFFDF8FF),
    onSurface = Color(0xFF23222B),
    surfaceVariant = Color(0xFFE9E1F5),
    onSurfaceVariant = Color(0xFF63616F),
    error = Color(0xFFB4423A),
    errorContainer = Color(0xFFFCE9E7),
    onErrorContainer = Color(0xFF7A241E)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB9A5F5),
    onPrimary = Color(0xFF2A1C5E),
    primaryContainer = Color(0xFF4A32A3),
    onPrimaryContainer = Color(0xFFE7DEFF),
    secondary = Color(0xFF5E9FE8),
    onSecondary = Color(0xFF00325A),
    secondaryContainer = Color(0xFF1C4875),
    onSecondaryContainer = Color(0xFFD3E4FF),
    tertiary = Color(0xFF72BC8F),
    onTertiary = Color(0xFF003920),
    tertiaryContainer = Color(0xFF175234),
    onTertiaryContainer = Color(0xFFB5F1CE),
    background = Color(0xFF12101C),
    onBackground = Color(0xFFF4F4F6),
    surface = Color(0xFF1A1726),
    onSurface = Color(0xFFF4F4F6),
    surfaceVariant = Color(0xFF2A2638),
    onSurfaceVariant = Color(0xFFBDB8CC),
    error = Color(0xFFF0968C),
    errorContainer = Color(0xFF5C1F1A),
    onErrorContainer = Color(0xFFFFDAD5)
)

private fun typographyWith(ff: FontFamily): Typography {
    val t = Typography()
    return Typography(
        displayLarge = t.displayLarge.copy(fontFamily = ff),
        displayMedium = t.displayMedium.copy(fontFamily = ff),
        displaySmall = t.displaySmall.copy(fontFamily = ff),
        headlineLarge = t.headlineLarge.copy(fontFamily = ff),
        headlineMedium = t.headlineMedium.copy(fontFamily = ff),
        headlineSmall = t.headlineSmall.copy(fontFamily = ff),
        titleLarge = t.titleLarge.copy(fontFamily = ff),
        titleMedium = t.titleMedium.copy(fontFamily = ff),
        titleSmall = t.titleSmall.copy(fontFamily = ff),
        bodyLarge = t.bodyLarge.copy(fontFamily = ff),
        bodyMedium = t.bodyMedium.copy(fontFamily = ff),
        bodySmall = t.bodySmall.copy(fontFamily = ff),
        labelLarge = t.labelLarge.copy(fontFamily = ff),
        labelMedium = t.labelMedium.copy(fontFamily = ff),
        labelSmall = t.labelSmall.copy(fontFamily = ff)
    )
}

@Composable
fun DietrixApp() {
    val context = LocalContext.current
    val systemDark = isSystemInDarkTheme()
    var dark by remember { mutableStateOf(Store.loadDark(context, systemDark)) }
    val appFont = remember {
        try {
            FontFamily(Typeface.createFromAsset(context.assets, "IRANSansDN.ttf"))
        } catch (e: Exception) {
            FontFamily.Default
        }
    }
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = typographyWith(appFont),
        shapes = Shapes(
            extraSmall = RoundedCornerShape(10.dp),
            small = RoundedCornerShape(14.dp),
            medium = RoundedCornerShape(20.dp),
            large = RoundedCornerShape(26.dp),
            extraLarge = RoundedCornerShape(32.dp)
        )
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            MainScreen(dark = dark, onToggleDark = {
                dark = it
                Store.saveDark(context, it)
            })
        }
    }
}

private val SECTION_LABELS = listOf(
    "stats" to "📊 نتایج",
    "macros" to "⚡ درشت‌مغذی‌ها",
    "units" to "🧩 واحدهای غذایی",
    "meals" to "🍽 وعده‌ها",
    "ref" to "📖 راهنمای واحدها"
)

@Composable
fun MainScreen(dark: Boolean, onToggleDark: (Boolean) -> Unit) {
    val context = LocalContext.current

    var gender by rememberSaveable { mutableStateOf("male") }
    var goalIdx by rememberSaveable { mutableStateOf(2) }
    var actIdx by rememberSaveable { mutableStateOf(1) }
    var heightTxt by rememberSaveable { mutableStateOf("") }
    var weightTxt by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<CalcResult?>(null) }

    val savedPcts = remember { Store.loadPcts(context) }
    var pctCho by rememberSaveable { mutableStateOf(savedPcts.first.toString()) }
    var pctPro by rememberSaveable { mutableStateOf(savedPcts.second.toString()) }
    var pctFat by rememberSaveable { mutableStateOf(savedPcts.third.toString()) }

    var history by remember { mutableStateOf(Store.loadHistory(context)) }
    var sections by rememberSaveable { mutableStateOf(setOf("stats")) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onToggleDark(!dark) }) {
                    Text(if (dark) "☀️" else "🌙", fontSize = 20.sp)
                }
                Spacer(Modifier.weight(1f))
                AssistChip(onClick = {}, label = { Text("✨ محاسبه‌گر هوشمند رژیم غذایی") })
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(48.dp))
            }

            Text(
                "Dietrix",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    brush = Brush.linearGradient(listOf(Color(0xFF1F6FC2), Color(0xFF6A4BD4), Color(0xFF2F9464))),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                )
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "وزن ایده‌آل (IBW) و وزن تعدیل‌شده (AIBW) شما محاسبه می‌شود، کالری روزانه بر اساس آن تعیین شده و یک برنامه‌ی وعده‌ها و واحدهای غذایی متناسب ارائه می‌شود.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // ---------- Input card ----------
            Card(shape = MaterialTheme.shapes.extraLarge, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("جنسیت", style = MaterialTheme.typography.labelLarge)
                    SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                        SegmentedButton(
                            selected = gender == "male",
                            onClick = { gender = "male" },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                        ) { Text("👨 آقا") }
                        SegmentedButton(
                            selected = gender == "female",
                            onClick = { gender = "female" },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                        ) { Text("👩 خانم") }
                    }

                    DropdownField(
                        label = "هدف (کاهش یا افزایش وزن ماهانه)",
                        value = GOALS[goalIdx].label,
                        options = GOALS.map { it.label },
                        onSelect = { goalIdx = it }
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = heightTxt,
                            onValueChange = { heightTxt = it },
                            label = { Text("قد (سانتی‌متر)") },
                            placeholder = { Text("مثلاً ۱۷۰") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = weightTxt,
                            onValueChange = { weightTxt = it },
                            label = { Text("وزن فعلی (کیلوگرم)") },
                            placeholder = { Text("مثلاً ۸۲") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    DropdownField(
                        label = "سطح فعالیت روزانه",
                        value = ACTIVITIES[actIdx].label,
                        options = ACTIVITIES.map { it.label },
                        onSelect = { actIdx = it }
                    )

                    AnimatedVisibility(visible = error != null) {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Text(
                                error ?: "",
                                Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val h = parseNum(heightTxt)
                            val w = parseNum(weightTxt)
                            if (h == null || h < 120 || h > 220 || w == null || w < 30 || w > 250) {
                                error = "لطفاً قد را بین ۱۲۰ تا ۲۲۰ سانتی‌متر و وزن را بین ۳۰ تا ۲۵۰ کیلوگرم وارد کنید."
                            } else {
                                error = null
                                val r = calculate(gender, h, w, GOALS[goalIdx].deficit, ACTIVITIES[actIdx].factor)
                                result = r
                                val entry = HistEntry(System.currentTimeMillis(), gender, h, w, GOALS[goalIdx].short, Math.round(r.target))
                                history = (listOf(entry) + history).take(15)
                                Store.saveHistory(context, history)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("🧮 محاسبه کالری و برنامه غذایی", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }

            // ---------- Results ----------
            val r = result
            if (r != null) {
                Spacer(Modifier.height(20.dp))

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SECTION_LABELS.forEach { (key, label) ->
                        FilterChip(
                            selected = key in sections,
                            onClick = {
                                sections = if (key in sections) {
                                    val s = sections - key
                                    if (s.isEmpty()) setOf("stats") else s
                                } else sections + key
                            },
                            label = { Text(label) }
                        )
                    }
                    FilterChip(
                        selected = sections.size == SECTION_LABELS.size,
                        onClick = {
                            sections = if (sections.size == SECTION_LABELS.size) setOf("stats")
                            else SECTION_LABELS.map { it.first }.toSet()
                        },
                        label = { Text("✅ همه") }
                    )
                }

                if ("stats" in sections) {
                    SectionTitle("📊 نتایج محاسبه")
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("🏆 وزن ایده‌آل (IBW)", fa(r.ibw), "کیلوگرم", modifier = Modifier.weight(1f))
                        StatCard("⚖️ " + r.basisLabel, fa(r.basis), "کیلوگرم", modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard("🔥 کالری نگهدارنده (TEE)", fa(r.maint), "کیلوکالری در روز", modifier = Modifier.weight(1f))
                        StatCard("⚡ کالری هدف روزانه", fa(r.target), "کیلوکالری در روز", highlighted = true, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                    val note = "💡 " + r.basisNote + " انرژی نگهدارنده = " + fa(r.basis) +
                        (if (r.gender == "male") " × ۲۴" else " × ۲۴ × ۰٫۹") +
                        " × ۱٫۱ × " + faFactor(r.activity) + " = " + fa(r.maint) + " کیلوکالری." +
                        (if (r.deficit > 0) " برای هدف کاهش وزن، " + fa(r.deficit) + " کیلوکالری از آن کم شد."
                        else if (r.deficit < 0) " برای هدف افزایش وزن، " + fa(-r.deficit) + " کیلوکالری به آن اضافه شد." else "")
                    NoteCard(note)
                    if (r.floored) {
                        Spacer(Modifier.height(8.dp))
                        WarnCard("⚠️ کالری محاسبه‌شده کمتر از ۱۰۰۰ کیلوکالری بود؛ برای حفظ سلامت، برنامه روی حداقل ۱۰۰۰ کیلوکالری تنظیم شد.")
                    }
                }

                if ("macros" in sections) {
                    SectionTitle("⚡ درشت‌مغذی‌های روزانه")
                    Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text("⚙️ تنظیم درصدها:", style = MaterialTheme.typography.labelLarge)
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                PctField("🍞 کربوهیدرات", pctCho, { pctCho = it }, Modifier.weight(1f))
                                PctField("🍗 پروتئین", pctPro, { pctPro = it }, Modifier.weight(1f))
                                PctField("🥑 چربی", pctFat, { pctFat = it }, Modifier.weight(1f))
                            }
                            val cho = parseNum(pctCho)?.toInt() ?: 0
                            val pro = parseNum(pctPro)?.toInt() ?: 0
                            val fat = parseNum(pctFat)?.toInt() ?: 0
                            Store.savePcts(context, cho, pro, fat)
                            val sum = cho + pro + fat
                            if (sum != 100) {
                                Text(
                                    "⚠️ جمع درصدها " + fa(sum) + "٪ است؛ بهتر است ۱۰۰٪ باشد.",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            TextButton(onClick = { pctCho = "53"; pctPro = "17"; pctFat = "30" }) { Text("↺ بازنشانی") }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatCard("🍞 کربوهیدرات (" + fa(cho) + "٪)", fa(r.target * cho / 100.0 / 4.0), "گرم در روز", modifier = Modifier.weight(1f))
                                StatCard("🍗 پروتئین (" + fa(pro) + "٪)", fa(r.target * pro / 100.0 / 4.0), "گرم در روز", modifier = Modifier.weight(1f))
                                StatCard("🥑 چربی (" + fa(fat) + "٪)", fa(r.target * fat / 100.0 / 9.0), "گرم در روز", modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                if ("units" in sections) {
                    SectionTitle("🧩 واحدهای غذایی روزانه — نزدیک‌ترین برنامه: حدود " + fa(r.planKcal) + " کیلوکالری")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        planTotals(r.plan).forEach { (k, u) ->
                            val g = group(k)
                            AssistChip(onClick = {}, label = { Text(g.icon + " " + g.name + ": " + fa(u) + " واحد") })
                        }
                    }
                }

                if ("meals" in sections) {
                    SectionTitle("🍽 برنامه وعده‌های غذایی")
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        MEAL_NAMES.forEach { (mk, mname) ->
                            val meal = r.plan[mk] ?: return@forEach
                            Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text((MEAL_ICONS[mk] ?: "") + "  " + mname, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                        Spacer(Modifier.weight(1f))
                                        Text(
                                            "≈ " + fa(mealKcal(meal)) + " کیلوکالری",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        meal.forEach { (k, u) ->
                                            val g = group(k)
                                            AssistChip(onClick = {}, label = { Text(g.name + " × " + fa(u)) })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if ("ref" in sections) {
                    SectionTitle("📖 هر واحد غذایی یعنی چقدر؟")
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        GROUPS.forEach { g ->
                            Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(g.icon + "  " + g.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                    Text("هر واحد ≈ " + fa(g.kcal) + " کیلوکالری", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    (REF[g.key] ?: emptyList()).forEach { item ->
                                        Text("• " + item, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                FormulasCard()
                Spacer(Modifier.height(12.dp))
                WarnCard("این برنامه جنبه‌ی آموزشی و راهنمای کلی دارد و جایگزین مشاوره‌ی متخصص تغذیه نیست. در صورت بارداری، شیردهی، دیابت، بیماری کلیوی یا هر بیماری زمینه‌ای، حتماً با پزشک یا متخصص تغذیه مشورت کنید.")
            }

            // ---------- History ----------
            if (history.isNotEmpty()) {
                SectionTitle("🕘 تاریخچه محاسبات")
                Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.padding(8.dp)) {
                        history.forEachIndexed { idx, e ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(formatFaDate(e.ts), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        (if (e.gender == "male") "👨 آقا" else "👩 خانم") + " • 📏 " + fa(e.h) + " • ⚖️ " + fa(e.w) + " • 🎯 " + e.goal,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text("⚡ " + fa(e.target) + " کیلوکالری", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = {
                                    history = history.filterIndexed { i, _ -> i != idx }
                                    Store.saveHistory(context, history)
                                }) { Text("✕") }
                            }
                        }
                        TextButton(onClick = {
                            history = emptyList()
                            Store.saveHistory(context, history)
                        }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text("🗑 پاک کردن همه") }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun DropdownField(label: String, value: String, options: List<String>, onSelect: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEachIndexed { i, opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = {
                    onSelect(i)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun PctField(label: String, value: String, onChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    )
}

@Composable
fun SectionTitle(text: String) {
    Spacer(Modifier.height(24.dp))
    Text(text, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold))
    Spacer(Modifier.height(12.dp))
}

@Composable
fun StatCard(label: String, value: String, unit: String, highlighted: Boolean = false, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (highlighted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(unit, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun NoteCard(text: String) {
    Card(shape = MaterialTheme.shapes.medium, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Text(text, Modifier.padding(14.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun WarnCard(text: String) {
    Card(shape = MaterialTheme.shapes.medium, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Text(text, Modifier.padding(14.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer)
    }
}

@Composable
fun FormulasCard() {
    var open by remember { mutableStateOf(false) }
    Card(
        onClick = { open = !open },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ℹ️ فرمول‌های استفاده‌شده در محاسبه", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(Modifier.weight(1f))
                Text(if (open) "−" else "+", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (open) {
                val lines = listOf(
                    "وزن ایده‌آل (IBW): آقایان: ۲۳ × قد²(متر) | خانم‌ها: ۲۲ × قد²(متر)",
                    "وزن تعدیل‌شده (AIBW): اگر وزن فعلی بیشتر از وزن ایده‌آل باشد: AIBW = IBW + (وزن فعلی − IBW) ÷ ۳",
                    "انرژی نگهدارنده (E): وزن مبنا × ۲۴ (خانم‌ها × ۰٫۹) × ۱٫۱ × ضریب فعالیت (۱٫۲ تا ۱٫۵)",
                    "کسر کالری برای کاهش وزن: کاهش ۳ کیلو در ماه → ۷۰۰− کالری | کاهش ۴ کیلو در ماه → ۱۰۰۰− کالری",
                    "افزودن کالری برای افزایش وزن: افزایش ۲ کیلو در ماه → ۵۰۰+ کالری | افزایش ۳ کیلو در ماه → ۷۰۰+ کالری",
                    "درشت‌مغذی‌ها: پیش‌فرض ۵۳٪ کربوهیدرات و ۱۷٪ پروتئین (هر گرم ۴ کالری) | ۳۰٪ چربی (هر گرم ۹ کالری)"
                )
                lines.forEach { Text("• " + it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        }
    }
}
