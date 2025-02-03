package Praktikum.Pemrograman.Mobile.Lisvindanu.Pertemuan1

import java.util.*

class PersegiPanjang(val panjang: Int? = null, val lebar: Int ? = null) {
        fun hitungLuas (panjang : Int, lebar: Int): Int {
             return panjang * lebar
        }

        fun hitungKeliling (panjang: Int, lebar: Int):Int {
            return 2 * (panjang +lebar)
        }

    }

    fun main() {

        //Menggunakan Scanner
        val scanner = Scanner(System.`in`)

        print("Masukkan Nilai Panjang : ")
        val panjang = scanner.nextInt()

        print("Masukkan Nilai lebar : ")
        val lebar = scanner.nextInt()

        val persegiPanjang1 = PersegiPanjang()
        println("Hasil Luas Persegi Panjang "+ persegiPanjang1.hitungLuas(panjang, lebar))
        println("Hasil Keliling Persegi Panjang" + persegiPanjang1.hitungKeliling(panjang, lebar))


        println("=================================")
        //Menggunakan Parameter Argument pada constructor

        val persegiPanjang2 = PersegiPanjang(4, 2)
        println("Nilai Panjang ${persegiPanjang2.panjang} ")
        println("Nilai Lebar ${persegiPanjang2.lebar} ")
        println("Hasil Luas Persegi Panjang "+ persegiPanjang2.hitungLuas(panjang, lebar))
        println("Hasil Keliling Persegi Panjang " + persegiPanjang2.hitungKeliling(panjang, lebar))

        println("=================================")

        // Menggunakan Nilai langsung di fungsi (tanpa properti kelas)
        val persegiPanjang3 = PersegiPanjang()
        println("Hasil Luas Persegi Panjang "+ persegiPanjang3.hitungLuas(4, 2))
        println("Hasil Keliling Persegi Panjang " + persegiPanjang3.hitungKeliling(4, 2))
    }

