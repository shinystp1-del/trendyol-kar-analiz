
package com.shsgroup.trendyolkaranaliz

import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var isTurkish = true

    private lateinit var title: TextView
    private lateinit var buyLabel: TextView
    private lateinit var saleLabel: TextView
    private lateinit var buyPrice: EditText
    private lateinit var salePrice: EditText
    private lateinit var result: TextView
    private lateinit var calculateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scroll = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        title = TextView(this).apply {
            textSize = 26f
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 35)
        }
        layout.addView(title)

        val languages = LinearLayout(this).apply {
            gravity = Gravity.CENTER
            orientation = LinearLayout.HORIZONTAL
        }

        val trButton = Button(this).apply {
            text = "Türkçe"
            setOnClickListener {
                isTurkish = true
                updateLanguage()
            }
        }

        val faButton = Button(this).apply {
            text = "فارسی"
            setOnClickListener {
                isTurkish = false
                updateLanguage()
            }
        }

        languages.addView(trButton)
        languages.addView(faButton)
        layout.addView(languages)

        buyLabel = TextView(this)
        buyLabel.textSize = 18f
        layout.addView(buyLabel)

        buyPrice = EditText(this).apply {
            inputType = 8194
        }
        layout.addView(buyPrice)

        saleLabel = TextView(this).apply {
            textSize = 18f
        }
        layout.addView(saleLabel)

        salePrice = EditText(this).apply {
            inputType = 8194
        }
        layout.addView(salePrice)

        calculateButton = Button(this)
        calculateButton.setOnClickListener {
            calculateProfit()
        }
        layout.addView(calculateButton)

        result = TextView(this).apply {
            textSize = 22f
            gravity = Gravity.CENTER
            setPadding(0, 40, 0, 20)
        }
        layout.addView(result)

        scroll.addView(layout)
        setContentView(scroll)

        updateLanguage()
    }

    private fun updateLanguage() {
        title.text = if (isTurkish)
            "Trendyol Kâr Analiz"
        else
            "تحلیل سود ترندیول"

        buyLabel.text = if (isTurkish)
            "Alış Fiyatı (TL)"
        else
            "قیمت خرید (لیر)"

        saleLabel.text = if (isTurkish)
            "Satış Fiyatı (TL)"
        else
            "قیمت فروش (لیر)"

        buyPrice.hint = if (isTurkish)
            "Ürün alış fiyatını girin"
        else
            "قیمت خرید محصول را وارد کنید"

        salePrice.hint = if (isTurkish)
            "Ürün satış fiyatını girin"
        else
            "قیمت فروش محصول را وارد کنید"

        calculateButton.text = if (isTurkish)
            "Kâr Hesapla"
        else
            "محاسبه سود"
    }

    private fun calculateProfit() {
        val buy = buyPrice.text.toString().toDoubleOrNull()
        val sale = salePrice.text.toString().toDoubleOrNull()

        if (buy == null || sale == null) {
            result.text = if (isTurkish)
                "Lütfen geçerli fiyatlar girin."
            else
                "لطفاً قیمت‌های معتبر وارد کنید."
            return
        }

        val profit = sale - buy

        result.text = if (isTurkish)
            "Fiyat farkı: %.2f TL".format(profit)
        else
            "اختلاف قیمت: %.2f لیر".format(profit)
    }
}
