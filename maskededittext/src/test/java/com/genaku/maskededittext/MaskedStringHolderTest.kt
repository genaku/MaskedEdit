package com.genaku.maskededittext

import org.junit.Test
import org.junit.runner.RunWith
import pl.mareklangiewicz.uspek.USpekRunner
import pl.mareklangiewicz.uspek.o
import pl.mareklangiewicz.uspek.uspek

@RunWith(USpekRunner::class)
class MaskedStringHolderTest {

    @Test
    fun test() = uspek {
        "holder without mask" o {
            val holder = MaskedStringHolder("")
            val initialText = "012345"
            "insert" o {
                "in range" o {
                    holder.replaceUnmaskedChars(0, 1, "s")
                    holder.unmasked eqq "s"
                }
                "out range" o {
                    holder.unmasked = initialText
                    holder.replaceUnmaskedChars(9, 10, "s")
                    holder.unmasked eqq initialText
                }
            }
            "replace" o {
                holder.unmasked = initialText
                "in range" o {
                    holder.replaceUnmaskedChars(2, 3, "9")
                    holder.unmasked eqq "019345"
                }
                "out range" o {
                    holder.replaceUnmaskedChars(7, 8, "9")
                    holder.unmasked eqq initialText
                }
                "less chars" o {
                    holder.replaceUnmaskedChars(2, 5, "a")
                    holder.unmasked eqq "01a5"
                }
                "more chars" o {
                    holder.replaceUnmaskedChars(2, 3, "aaa")
                    holder.unmasked eqq "01aaa345"
                }
            }
            "delete" o {
                holder.unmasked = initialText
                "in range" o {
                    holder.deleteUnmaskedChars(1, 2)
                    holder.unmasked eqq "02345"
                }
                "out range" o {
                    holder.deleteUnmaskedChars(8, 9)
                    holder.unmasked eqq initialText
                }
                "cross range" o {
                    holder.deleteUnmaskedChars(3, 9)
                    holder.unmasked eqq "012"
                }
            }
        }
        "mask" o {
            val holder = MaskedStringHolder(TEST_MASK)
            "insert first digit" o {
                holder.replaceUnmaskedChars(0, 1, "9")
                holder.unmasked eqq "9"
                holder.formatted.toString() eqq "+7(9"
            }
            "insert 3 digits" o {
                holder.replaceUnmaskedChars(0, 3, "902")
                holder.unmasked eqq "902"
                holder.formatted.toString() eqq "+7(902)"
            }
            "insert 4 digits" o {
                holder.replaceUnmaskedChars(0, 4, "9026")
                holder.unmasked eqq "9026"
                holder.formatted.toString() eqq "+7(902)6"
            }
            "insert 4 digits in sequence" o {
                holder.replaceUnmaskedChars(0, 1, "9")
                holder.replaceUnmaskedChars(1, 2, "0")
                holder.replaceUnmaskedChars(2, 3, "2")
                holder.replaceUnmaskedChars(3, 4, "6")
                holder.unmasked eqq "9026"
                holder.formatted.toString() eqq "+7(902)6"
            }
            "delete last digit" o {
                holder.unmasked = "9026"
                holder.deleteUnmaskedChars(3, 4)
                holder.unmasked eqq "902"
                holder.formatted.toString() eqq "+7(902)"
            }
            "empty unmasked should show empty formatted" o {
                holder.unmasked = ""
                holder.formatted.toString() eqq ""
            }
        }
        "mask pos converter" o {
            val mask = Mask(TEST_MASK)
            val input = "+7(902)6**"
            val raw = "9026"
            "get out of pos" o {
                mask.convertPos(input, 10) eqq 4
            }
            "get first pos" o {
                mask.convertPos(input, 3) eqq 0
                raw.substring(0, 1) eqq "9"
            }
            "get second pos" o {
                mask.convertPos(input, 4) eqq 1
                raw.substring(1, 2) eqq "0"
            }
            "get last input pos" o {
                mask.convertPos(input, 7) eqq 4
                raw.substring(3, 4) eqq "6"
            }
            "get last input pos after fixed char" o {
                mask.convertPos("+7(123)", 7) eqq 3
            }
            "get first pos from 0" o {
                mask.convertPos("+7(123)", 0) eqq 0
            }
            "get first pos from 1" o {
                mask.convertPos("+7(123)", 1) eqq 0
            }
            "get last pos on delete" o {
                mask.convertPos("+7(44", 4, true) eqq 1
            }
            "get last pos on delete 1" o {
                mask.convertPos("+7(44", 5, true) eqq 2
            }
            "get last pos on delete 2" o {
                mask.convertPos("+7(902)6**", 8, true) eqq 4
            }
        }
        "delete in masked" o {
            val holder = MaskedStringHolder(TEST_MASK)
            val input = "+7(902)6**"
            val formatted = "+7(902)6"
            val raw = "9026"
            holder.unmasked = raw
            "delete in prepopulated" o {
                holder.deleteChars(input, 0, 1)
                holder.unmasked eqq raw
                holder.formatted.toString() eqq formatted
            }
            "delete first digit" o {
                holder.deleteChars(input, 3, 4)
                holder.unmasked eqq "026"
                holder.formatted.toString() eqq "+7(026)"
            }
            "delete last char" o {
                holder.unmasked = "44"
                holder.deleteChars("+7(44", 4, 5)
                holder.unmasked eqq "4"
                holder.formatted.toString() eqq "+7(4"
            }
            "delete last input digit" o {
                holder.deleteChars(input, 7, 8)
                holder.unmasked eqq "902"
                holder.formatted.toString() eqq "+7(902)"
            }
            "delete out of input" o {
                holder.deleteChars(input, 10, 11)
                holder.unmasked eqq raw
                holder.formatted.toString() eqq formatted
            }
            "delete out of field" o {
                holder.deleteChars(input, 20, 21)
                holder.unmasked eqq raw
                holder.formatted.toString() eqq formatted
            }
        }
        "delete in other mask" o {
            val holder = MaskedStringHolder("+7(###) ###-##-##")
            holder.unmasked = "1234567890"
            "delete char at the end" o {
                holder.deleteChars("+7(123) 456-78-90", 16, 17)
                holder.unmasked eqq "123456789"
            }
            "delete char at the end 1" o {
                holder.deleteChars("+7(123) 456-78-90", 15, 16)
                holder.unmasked eqq "123456780"
            }
        }
        "replace in masked" o {
            val holder = MaskedStringHolder(TEST_MASK)
            val input = "+7(902)6**"
            val formatted = "+7(902)6"
            val raw = "9026"
            holder.unmasked = raw
            "replace in prepopulated" o {
                holder.replaceChars(input, 0, 1, "0")
                holder.unmasked eqq "09026"
                holder.formatted.toString() eqq "+7(090)26"
            }
            "replace first digit" o {
                holder.replaceChars(input, 3, 4, "0")
                holder.unmasked eqq "0026"
                holder.formatted.toString() eqq "+7(002)6"
            }
            "replace last input digit" o {
                holder.replaceChars(input, 7, 8, "0")
                holder.unmasked eqq "9020"
                holder.formatted.toString() eqq "+7(902)0"
            }
            "replace out of input" o {
                holder.replaceChars(input, 10, 11, "0")
                holder.unmasked eqq "90260"
                holder.formatted.toString() eqq "+7(902)60"
            }
            "replace out of field" o {
                holder.replaceChars(input, 20, 21, "0")
                holder.unmasked eqq "90260"
                holder.formatted.toString() eqq "+7(902)60"
            }
        }
        "insert in masked" o {
            val holder = MaskedStringHolder(TEST_MASK)
            val input = "+7(902)6**"
            val formatted = "+7(902)6"
            val raw = "9026"
            holder.unmasked = raw
            "insert in prepopulated" o {
                holder.replaceChars(input, 0, 0, "0")
                holder.unmasked eqq "09026"
                holder.formatted.toString() eqq "+7(090)26"
            }
            "insert first digit" o {
                holder.replaceChars(input, 3, 3, "0")
                holder.unmasked eqq "09026"
                holder.formatted.toString() eqq "+7(090)26"
            }
            "insert last input digit" o {
                holder.replaceChars(input, 7, 7, "0")
                holder.unmasked eqq "90260"
                holder.formatted.toString() eqq "+7(902)60"
            }
            "insert out of input" o {
                holder.replaceChars(input, 10, 10, "0")
                holder.unmasked eqq "90260"
                holder.formatted.toString() eqq "+7(902)60"
            }
            "insert out of field" o {
                holder.replaceChars(input, 20, 20, "0")
                holder.unmasked eqq "90260"
                holder.formatted.toString() eqq "+7(902)60"
            }
        }
        "add in masked" o {
            val holder = MaskedStringHolder(TEST_MASK)
            "add second digit" o {
                holder.unmasked = "1"
                holder.replaceChars("+7(1", 4, 4, "2")
                holder.unmasked eqq "12"
                holder.formatted.toString() eqq "+7(12"
            }
            "add 3rd digit" o {
                holder.unmasked = "12"
                holder.replaceChars("+7(12", 5, 5, "3")
                holder.unmasked eqq "123"
                holder.formatted.toString() eqq "+7(123)"
            }
            "add 4th digit" o {
                holder.unmasked = "123"
                holder.replaceChars("+7(123)", 7, 7, "4")
                holder.unmasked eqq "1234"
                holder.formatted.toString() eqq "+7(123)4"
            }
        }
    }

    @Test
    fun testMask() = uspek {
        "cursor positions" o {
            val mask = Mask(TEST_MASK)
            "list of positions" o {
                mask.validCursorPositions eqq listOf(3, 4, 5, 7, 8, 9, 10)
            }
            "find first pos" o {
                val pos = mask.getNextPosition(0, false)
                pos eqq 4
            }
            "find first pos 1" o {
                val pos = mask.getNextPosition(3, false)
                pos eqq 4
            }
            "find next pos 1" o {
                val pos = mask.getNextPosition(4, false)
                pos eqq 5
            }
            "find next pos 2" o {
                val pos = mask.getNextPosition(5, false)
                pos eqq 7
            }
            "find next pos 3" o {
                val pos = mask.getNextPosition(6, false)
                pos eqq 7
            }
            "find pos on deletion 1" o {
                val pos = mask.getNextPosition(4, true)
                pos eqq 4
            }
            "find pos on deletion 2" o {
                val pos = mask.getNextPosition(3, true)
                pos eqq 3
            }
            "find pos on deletion 3" o {
                val pos = mask.getNextPosition(2, true)
                pos eqq 3
            }
            "find pos on deletion 4" o {
                val pos = mask.getNextPosition(7, true)
                pos eqq 7
            }
        }
        "some pos cases" o {
            val mask = Mask("+7(###) ###-##-##")
            "pos after last char delete" o {
                mask.getNextPosition(16, true) eqq 16
            }
            "pos after delete after fixed char" o {
                mask.getNextPosition(15, true) eqq 15
            }
            "replace last char" o {
                val holder = MaskedStringHolder("+7(###) ###-##-##")
                holder.unmasked = "1234555556"
                holder.replaceChars("+7(123) 455-55-56", 16, 16, "3")
                holder.unmasked eqq "1234555553"
            }
        }
    }

    companion object {
        const val TEST_MASK = "+7(###)###"
    }
}