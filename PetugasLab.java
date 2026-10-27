public class PetugasLab extends PenggunaLab {
  private final String nip;

  public PetugasLab(String id, String nama, String nip){
    super(id, nama);
    this.nip = nip;
  }
  
  // getter
  public String getNip() { return nip; }

  public void periksaKelayakan(AlatPraktikum alat, boolean layakPakai) {
    alat.setLayakPakai(layakPakai) ;
  }

  @Override
  public int getMaksKuotaPinjam(){
    return SistemInventarisLab.MAKS_PINJAM;
  }

}
