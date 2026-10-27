public abstract class PenggunaLab {
  private final String id;
  private final String nama;

  public PenggunaLab(String id, String nama){
    this.id   = id;
    this.nama = nama;
  }
  
  // Getter
  public String getId() { return id; }
  public String getNama() { return nama; }

  public abstract int getMaksKuotaPinjam();
}
