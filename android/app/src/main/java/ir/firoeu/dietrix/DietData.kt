package ir.firoeu.dietrix

import android.content.Context
import androidx.compose.ui.graphics.Color
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToLong

data class FoodGroup(val key: String, val name: String, val kcal: Int, val color: Color, val icon: String)

val GROUPS = listOf(
    FoodGroup("bread", "نان و غلات", 80, Color(0xFFD5803B), "🍞"),
    FoodGroup("meat", "گوشت و جانشین‌ها", 55, Color(0xFFE56458), "🍗"),
    FoodGroup("milk", "شیر و لبنیات", 120, Color(0xFF5E9FE8), "🥛"),
    FoodGroup("fruit", "میوه", 60, Color(0xFFBF8EDA), "🍎"),
    FoodGroup("veg", "سبزی", 25, Color(0xFF46A171), "🥦"),
    FoodGroup("fat", "چربی", 45, Color(0xFFEAC26B), "🫒")
)

fun group(key: String): FoodGroup = GROUPS.first { it.key == key }

val REF = mapOf(
    "bread" to listOf("یک کف دست نان سنگک، بربری یا تافتون (۳۰ گرم)", "۴ کف دست نان لواش", "نصف لیوان برنج یا ماکارونی پخته", "یک عدد سیب‌زمینی متوسط"),
    "meat" to listOf("۳۰ گرم گوشت قرمز، مرغ یا ماهی پخته (به اندازه یک قوطی کبریت)", "یک عدد تخم‌مرغ", "۳۰ گرم پنیر (یک قوطی کبریت)", "نصف لیوان حبوبات پخته"),
    "milk" to listOf("یک لیوان شیر کم‌چرب", "یک لیوان ماست کم‌چرب", "نصف لیوان کشک"),
    "fruit" to listOf("یک عدد سیب، پرتقال یا هلوی متوسط", "نصف موز بزرگ", "یک لیوان هندوانه یا طالبی خردشده", "۳ عدد خرما"),
    "veg" to listOf("یک لیوان سبزی خام (کاهو، خیار، گوجه و...)", "نصف لیوان سبزی پخته", "یک عدد گوجه‌فرنگی متوسط"),
    "fat" to listOf("یک قاشق چای‌خوری روغن مایع یا روغن زیتون", "۵ عدد بادام یا فندق", "۱۰ عدد پسته", "یک قاشق غذاخوری تخمه")
)

val MEAL_NAMES = linkedMapOf(
    "b" to "صبحانه",
    "ms" to "میان‌وعده صبح",
    "l" to "ناهار",
    "me" to "میان‌وعده عصر",
    "d" to "شام",
    "mn" to "میان‌وعده شب"
)

val MEAL_ICONS = mapOf("b" to "🌅", "ms" to "🍵", "l" to "🍽️", "me" to "🍏", "d" to "🌙", "mn" to "✨")

val PLANS: Map<Int, Map<String, Map<String, Int>>> = mapOf(
    1000 to mapOf(
        "b" to mapOf("bread" to 1, "meat" to 1, "fat" to 1),
        "ms" to mapOf("fruit" to 1),
        "l" to mapOf("bread" to 2, "meat" to 1, "veg" to 2, "fat" to 1),
        "me" to mapOf("milk" to 1),
        "d" to mapOf("bread" to 1, "meat" to 1, "veg" to 1),
        "mn" to mapOf("milk" to 1, "fruit" to 1)
    ),
    1200 to mapOf(
        "b" to mapOf("bread" to 2, "meat" to 1, "fat" to 1),
        "ms" to mapOf("fruit" to 1),
        "l" to mapOf("bread" to 2, "meat" to 1, "veg" to 2, "fat" to 1),
        "me" to mapOf("milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 1, "meat" to 1, "veg" to 1, "fat" to 1),
        "mn" to mapOf("milk" to 1, "fruit" to 1)
    ),
    1400 to mapOf(
        "b" to mapOf("bread" to 2, "meat" to 1, "fat" to 1),
        "ms" to mapOf("fruit" to 1),
        "l" to mapOf("bread" to 2, "meat" to 2, "veg" to 2, "fat" to 2),
        "me" to mapOf("milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 2, "meat" to 1, "veg" to 2, "fat" to 1),
        "mn" to mapOf("milk" to 1, "fruit" to 1)
    ),
    1600 to mapOf(
        "b" to mapOf("bread" to 2, "meat" to 1, "fat" to 1, "fruit" to 1),
        "ms" to mapOf("bread" to 1, "fruit" to 1),
        "l" to mapOf("bread" to 2, "meat" to 2, "veg" to 2, "fat" to 2),
        "me" to mapOf("milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 2, "meat" to 2, "veg" to 2, "fat" to 2),
        "mn" to mapOf("milk" to 1, "fruit" to 1)
    ),
    1800 to mapOf(
        "b" to mapOf("bread" to 2, "milk" to 1, "meat" to 1, "fat" to 1),
        "ms" to mapOf("bread" to 1, "fruit" to 1),
        "l" to mapOf("bread" to 3, "meat" to 2, "veg" to 2, "fat" to 2),
        "me" to mapOf("milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 2, "meat" to 2, "veg" to 3, "fat" to 1),
        "mn" to mapOf("milk" to 1, "fruit" to 2)
    ),
    2000 to mapOf(
        "b" to mapOf("bread" to 3, "milk" to 1, "meat" to 1, "fat" to 1),
        "ms" to mapOf("bread" to 1, "fruit" to 1),
        "l" to mapOf("bread" to 3, "meat" to 3, "veg" to 2, "fat" to 2),
        "me" to mapOf("milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 2, "meat" to 2, "veg" to 3, "fat" to 2),
        "mn" to mapOf("milk" to 1, "fruit" to 2)
    ),
    2200 to mapOf(
        "b" to mapOf("bread" to 3, "milk" to 1, "meat" to 1, "fat" to 1, "fruit" to 1),
        "ms" to mapOf("bread" to 1, "fruit" to 1),
        "l" to mapOf("bread" to 3, "meat" to 3, "veg" to 2, "fat" to 2),
        "me" to mapOf("bread" to 1, "milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 2, "meat" to 3, "veg" to 3, "fat" to 3),
        "mn" to mapOf("milk" to 1, "fruit" to 2)
    ),
    2500 to mapOf(
        "b" to mapOf("bread" to 3, "milk" to 1, "meat" to 2, "fat" to 1, "fruit" to 1),
        "ms" to mapOf("bread" to 2, "fruit" to 1),
        "l" to mapOf("bread" to 4, "meat" to 3, "veg" to 3, "fat" to 3),
        "me" to mapOf("bread" to 1, "milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 2, "meat" to 3, "veg" to 3, "fat" to 3),
        "mn" to mapOf("milk" to 1, "fruit" to 2)
    ),
    2800 to mapOf(
        "b" to mapOf("bread" to 3, "milk" to 1, "meat" to 2, "fat" to 2, "fruit" to 1),
        "ms" to mapOf("bread" to 2, "fruit" to 1, "fat" to 1),
        "l" to mapOf("bread" to 4, "meat" to 4, "veg" to 3, "fat" to 3),
        "me" to mapOf("bread" to 1, "milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 3, "meat" to 3, "veg" to 3, "fat" to 3),
        "mn" to mapOf("milk" to 1, "fruit" to 2)
    ),
    3000 to mapOf(
        "b" to mapOf("bread" to 4, "milk" to 1, "meat" to 2, "fat" to 2, "fruit" to 1),
        "ms" to mapOf("bread" to 2, "fruit" to 1, "fat" to 1),
        "l" to mapOf("bread" to 4, "meat" to 4, "veg" to 3, "fat" to 3),
        "me" to mapOf("bread" to 1, "milk" to 1, "fruit" to 1),
        "d" to mapOf("bread" to 3, "meat" to 3, "veg" to 3, "fat" to 3),
        "mn" to mapOf("bread" to 1, "milk" to 1, "fruit" to 2)
    )
)

data class GoalOption(val deficit: Int, val label: String, val short: String)

val GOALS = listOf(
    GoalOption(0, "تثبیت وزن (بدون کسر کالری)", "تثبیت وزن"),
    GoalOption(700, "کاهش ۳ کیلوگرم در ماه (−۷۰۰ کالری)", "کاهش ۳ کیلو (−۷۰۰)"),
    GoalOption(1000, "کاهش ۴ کیلوگرم در ماه (−۱۰۰۰ کالری)", "کاهش ۴ کیلو (−۱۰۰۰)"),
    GoalOption(-500, "افزایش ۲ کیلوگرم در ماه (+۵۰۰ کالری)", "افزایش ۲ کیلو (+۵۰۰)"),
    GoalOption(-700, "افزایش ۳ کیلوگرم در ماه (+۷۰۰ کالری)", "افزایش ۳ کیلو (+۷۰۰)")
)

data class ActivityOption(val factor: Double, val label: String)

val ACTIVITIES = listOf(
    ActivityOption(1.2, "بی‌تحرک / استراحت — ضریب ۱٫۲"),
    ActivityOption(1.3, "فعالیت سبک (کار نشسته، پیاده‌روی کم) — ضریب ۱٫۳"),
    ActivityOption(1.4, "فعالیت متوسط (ورزش منظم یا کار نیمه‌فعال) — ضریب ۱٫۴"),
    ActivityOption(1.5, "فعالیت زیاد (کار بدنی یا ورزش سنگین) — ضریب ۱٫۵")
)

fun fa(n: Long): String {
    val s = String.format(Locale.US, "%,d", n)
    val sb = StringBuilder()
    for (c in s) sb.append(
        when {
            c in '0'..'9' -> '۰' + (c - '0')
            c == ',' -> '٬'
            else -> c
        }
    )
    return sb.toString()
}

fun fa(n: Double): String = fa(n.roundToLong())

fun fa(n: Int): String = fa(n.toLong())

fun faFactor(d: Double): String {
    val s = if (d % 1.0 == 0.0) d.toInt().toString() else d.toString()
    val sb = StringBuilder()
    for (c in s) sb.append(
        when {
            c in '0'..'9' -> '۰' + (c - '0')
            c == '.' -> '٫'
            else -> c
        }
    )
    return sb.toString()
}

data class CalcResult(
    val ibw: Double,
    val basis: Double,
    val basisLabel: String,
    val basisNote: String,
    val maint: Double,
    val target: Double,
    val floored: Boolean,
    val deficit: Int,
    val activity: Double,
    val gender: String,
    val planKey: Int,
    val plan: Map<String, Map<String, Int>>,
    val planKcal: Int
)

fun mealKcal(meal: Map<String, Int>): Int = meal.entries.sumOf { group(it.key).kcal * it.value }

fun planTotals(plan: Map<String, Map<String, Int>>): Map<String, Int> {
    val t = LinkedHashMap<String, Int>()
    for (meal in plan.values) for ((k, u) in meal) t[k] = (t[k] ?: 0) + u
    return t
}

fun calculate(gender: String, h: Double, w: Double, deficit: Int, activity: Double): CalcResult {
    val hm = h / 100.0
    val ibw = (if (gender == "male") 23 else 22) * hm * hm
    val basis: Double
    val basisLabel: String
    val basisNote: String
    if (w > ibw) {
        basis = ibw + (w - ibw) / 3
        basisLabel = "وزن تعدیل‌شده (AIBW)"
        basisNote = "چون وزن فعلی شما بیشتر از وزن ایده‌آل است، یک‌سوم اختلاف به وزن ایده‌آل اضافه و وزن تعدیل‌شده (AIBW) مبنای محاسبه شد."
    } else if (w < ibw) {
        basis = w
        basisLabel = "وزن فعلی (مبنا)"
        basisNote = "چون وزن فعلی شما کمتر از وزن ایده‌آل است، کالری بر اساس وزن فعلی محاسبه شد."
    } else {
        basis = ibw
        basisLabel = "وزن ایده‌آل (مبنا)"
        basisNote = "وزن شما در محدوده‌ی وزن ایده‌آل است و کالری بر اساس وزن ایده‌آل محاسبه شد."
    }
    val genderFactor = if (gender == "male") 1.0 else 0.9
    val maint = basis * 24 * genderFactor * 1.1 * activity
    var target = maint - deficit
    val floored = target < 1000
    if (floored) target = 1000.0
    val planKey = PLANS.keys.minByOrNull { abs(it - target) }!!
    val plan = PLANS[planKey]!!
    val planKcal = plan.values.sumOf { mealKcal(it) }
    return CalcResult(ibw, basis, basisLabel, basisNote, maint, target, floored, deficit, activity, gender, planKey, plan, planKcal)
}

data class HistEntry(val ts: Long, val gender: String, val h: Double, val w: Double, val goal: String, val target: Long)

object Store {
    private fun prefs(c: Context) = c.getSharedPreferences("dietrix", Context.MODE_PRIVATE)

    fun loadHistory(c: Context): List<HistEntry> = try {
        val arr = JSONArray(prefs(c).getString("history", "[]"))
        (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            HistEntry(o.getLong("ts"), o.getString("gender"), o.getDouble("h"), o.getDouble("w"), o.getString("goal"), o.getLong("target"))
        }
    } catch (e: Exception) {
        emptyList()
    }

    fun saveHistory(c: Context, list: List<HistEntry>) {
        val arr = JSONArray()
        list.take(15).forEach { e ->
            arr.put(
                JSONObject()
                    .put("ts", e.ts)
                    .put("gender", e.gender)
                    .put("h", e.h)
                    .put("w", e.w)
                    .put("goal", e.goal)
                    .put("target", e.target)
            )
        }
        prefs(c).edit().putString("history", arr.toString()).apply()
    }

    fun loadDark(c: Context, def: Boolean): Boolean = prefs(c).getBoolean("dark", def)
    fun saveDark(c: Context, v: Boolean) = prefs(c).edit().putBoolean("dark", v).apply()

    fun loadPcts(c: Context): Triple<Int, Int, Int> {
        val p = prefs(c)
        return Triple(p.getInt("pctCho", 53), p.getInt("pctPro", 17), p.getInt("pctFat", 30))
    }

    fun savePcts(c: Context, cho: Int, pro: Int, fat: Int) {
        prefs(c).edit().putInt("pctCho", cho).putInt("pctPro", pro).putInt("pctFat", fat).apply()
    }
}

fun parseNum(s: String): Double? {
    if (s.isBlank()) return null
    val sb = StringBuilder()
    for (c in s.trim()) {
        when {
            c in '۰'..'۹' -> sb.append('0' + (c - '۰'))
            c in '٠'..'٩' -> sb.append('0' + (c - '٠'))
            c == '٫' || c == ',' -> sb.append('.')
            else -> sb.append(c)
        }
    }
    return sb.toString().toDoubleOrNull()
}

fun formatFaDate(ts: Long): String = try {
    val df = android.icu.text.SimpleDateFormat("d MMMM yyyy — HH:mm", android.icu.util.ULocale("fa_IR@calendar=persian"))
    df.format(java.util.Date(ts))
} catch (e: Exception) {
    java.text.SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US).format(java.util.Date(ts))
}
