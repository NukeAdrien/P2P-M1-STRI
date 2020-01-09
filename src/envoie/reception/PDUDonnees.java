package envoie.reception;

public class PDUDonnees extends PDU {
	byte[] bloc;
	Integer index;
	public PDUDonnees(String type, String donnees,Integer index,byte[] bloc) {
		super(type, donnees);
		this.bloc = bloc;
		this.index = index;
	}
	public byte[] getBloc() {
		return bloc;
	}
	public void setBloc(byte[] bloc) {
		this.bloc = bloc;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}

}
