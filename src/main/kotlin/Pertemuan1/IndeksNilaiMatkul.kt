package Praktikum.Pemrograman.Mobile.Lisvindanu.Pertemuan1

data class IndeksNilaiMatkul(val ipk: Int? = null,
                        val nama: String,
                        val nrp: String)
fun Int?.nilaiKeGrade() : String {
    return when  (this){
        null -> "Nilai tidak boleh kosong"
        in 80..100 -> "A"
        in 70..79 -> "AB"
        in 60..69 -> "B"
        in 50..59 -> "BC"
        in 40..49 -> "C"
        in 30..39 -> "D"
        else -> "E"
    }
}


fun main() {
   val listMahasiswa = listOf(
       IndeksNilaiMatkul(100 ,"Lisvindanu", "223040038"),
       IndeksNilaiMatkul(75 ,"Sasuke", "223040098"),
       IndeksNilaiMatkul(50 ,"Naruto", "223040108"),
       IndeksNilaiMatkul(30, "Shikamaru", "223040088"),
       IndeksNilaiMatkul(null, "sakura", "223040088"),
   )

    for (mahasiswa in listMahasiswa){
        println("Nama ${mahasiswa.nama}, NRP: ${mahasiswa.nrp},Nilai ${mahasiswa.ipk ?: "kosong"}")
        println("Grade: ${mahasiswa.ipk.nilaiKeGrade()}")
        println("==================")
    }
}

