package com.example.kutu.util

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import java.util.*

class AppUtil {
    companion object {

        val colorList: List<String> =
            listOf("#F1AF2B", "#06C9AF", "#FF6969", "#6DC966", "#5D99FF", "#8899EC", "FF8963")


        fun getViewCount(): Int {
            val r = Random()
            return r.nextInt(6)
        }

        fun getAnswerList(message: String): MutableList<Int> {

            var answerList: MutableList<Int> = mutableListOf()

            val input: List<String> = message.split(" ")
            var count = 0
            var answer = 0
            var length = 0
            var check = false
            var pot = 0

            for (i in input.indices) {
                if (input[i].length == 4 || input[i].length == 6) {

                    length = input[i].length

                    answer = i
                    for (c in input[i].toCharArray()) {
                        check = false
                        pot = 1
                        if (Character.isDigit(c)) {
                            answer = i
                            check = true
                            pot = 0
                        } else {
                            count += input[i].length + 1
                            break
                        }
                    }
                    if (check) {
                        break
                    }
                } else {
                    count += input[i].length + 1
                    pot = 1

                }
            }
            answerList.add(count)
            answerList.add(length)
            answerList.add(pot)

            return answerList
        }

        fun getMessageText(mess: String, list: MutableList<Int>): SpannableString {


            var start: Int = list[0]
            var otpLength = list[1]
            var end: Int = start + (otpLength)


            val spannableString = SpannableString(mess)
            val color = ForegroundColorSpan(Color.BLACK)
            val style = StyleSpan(Typeface.BOLD)
//            val sizeSpan = RelativeSizeSpan(2f)

            spannableString.setSpan(color, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            spannableString.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//            spannableString.setSpan(sizeSpan,start,end,Spannable.SPAN_INCLUSIVE_INCLUSIVE)


            return spannableString

        }

    }
}