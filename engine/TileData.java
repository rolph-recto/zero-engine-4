package engine;

final class TileTemplate {
	private String name;
	private int index; //index of the image used in Tileset
	
	public TileTemplate(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

public class TileData {

}
