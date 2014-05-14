package mmm.littleMaidMob;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

/**
 * ターゲットにしているタイルのコンテナ
 *
 */
public class TileContainer {

	public List<int[]> tiles;


	public TileContainer() {
		tiles = new ArrayList<int[]>();
	}

	/**
	 * ブロックの登録
	 * @param pPosX
	 * @param pPosY
	 * @param pPosZ
	 * @return
	 */
	public int[] addTile(int pPosX, int pPosY, int pPosZ) {
		int[] lib = new int[] {pPosX, pPosY, pPosZ};
		tiles.add(lib);
		return lib;
	}
	public int[] addTile(int[] pPos) {
		return addTile(pPos[0], pPos[1], pPos[2]);
	}

	public int[] remove(int pPosX, int pPosY, int pPosZ) {
		int li = indexOf(pPosX, pPosY, pPosZ);
		return tiles.remove(li);
	}

	public boolean contain(int pPosX, int pPosY, int pPosZ) {
		for (int[] li : tiles) {
			if (li[0] == pPosX && li[1] == pPosY && li[2] == pPosZ) {
				return true;
			}
		}
		return false;
	}
	public boolean contain(int[] pPos) {
		return contain(pPos[0], pPos[1], pPos[2]);
	}

	public int indexOf(int pPosX, int pPosY, int pPosZ) {
		for (int[] li : tiles) {
			if (li[0] == pPosX && li[1] == pPosY && li[2] == pPosZ) {
				return tiles.indexOf(li);
			}
		}
		return -1;
	}

	public int[] get(int pIndex) {
		return tiles.get(pIndex);
	}

	public void clear() {
		tiles.clear();
	}

	public int size() {
		return tiles.size();
	}

	public void onUpdate(World pWorld) {
		// TIleEntityの生存チェック
		for (int[] li : tiles) {
			if (pWorld.getTileEntity(li[0], li[1], li[2]).isInvalid()) {
				// 対象は破壊されたので削除
				// TODO これいけるのか？
				tiles.remove(li);
			}
		}
	}

}
