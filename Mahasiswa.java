public class Mahasiswa extends PenggunaLab {
  private final String nim;

  public Mahasiswa(String id, String nama, String nim){
    super(id, nama);
    this.nim = nim;
  }

  // Getter section
  public String getNim() { return nim; }

  // Override abstract method and implement it
  @Override 
  public int getMaksKuotaPinjam() {
    return SistemInventarisLab.MAKS_PINJAM / 12;
  }
}
