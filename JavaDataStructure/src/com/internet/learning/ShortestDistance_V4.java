package com.internet.learning;

//��������������һ��ͼ�����·������  
public class ShortestDistance_V4 {
	public static int dijkstra(int[][] W1, int start, int end) {
		boolean[] isLabel = new boolean[W1[0].length];// �Ƿ���
		int min = Integer.MAX_VALUE;
		int[] indexs = new int[W1[0].length];// ���б�ŵĵ���±꼯��
		int i_count = -1;
		int index = start;// �ӳ�ʼ�㿪ʼ
		int presentShortest = 0;
		int[] distance = W1[start].clone();// v0���������̾���ĳ�ʼֵ
		indexs[++i_count] = index;// ���Ѿ���ŵ��±�����±꼯��
		isLabel[index] = true;
		while (true) {
			// ��һ�������v0,��w[0][0]�ҵ�����v0����ĵ�

			min = Integer.MAX_VALUE;
			for (int i = 0; i < distance.length; i++) {
				if (!isLabel[i] && distance[i] != -1 && i != index) {
					// �����������б�,����û�б����
					if (distance[i] < min) {
						min = distance[i];
						index = i;// ���±��Ϊ��ǰ�±�
					}
				}
			}
			if (index == end) {
				break;
			}
			isLabel[index] = true;
			indexs[++i_count] = index;// ���Ѿ���ŵ��±�����±꼯��
			if (W1[indexs[i_count - 1]][index] == -1
					|| presentShortest + W1[indexs[i_count - 1]][index] > distance[index]) {
				presentShortest = distance[index];
			} else {
				presentShortest += W1[indexs[i_count - 1]][index];
			}

			// �ڶ�������distance�еľ������vi
			for (int i = 0; i < distance.length; i++) {
				// ���vi���Ǹ����бߣ���v0�������ľ����
				// ����������������Σ� �Ǻ�
				if (distance[i] == -1 && W1[index][i] != -1) {// �����ǰ���ɴ�����ڿɴ���
					distance[i] = presentShortest + W1[index][i];
				} else if (W1[index][i] != -1
						&& presentShortest + W1[index][i] < distance[i]) {
					// �����ǰ�ɴ�����ڵ�·������ǰ���̣�������ɸ��̵�·��
					distance[i] = presentShortest + W1[index][i];
				}

			}
		}
		return distance[end] - distance[start];
	}

	public static int[][] getShortestPathMatrix(int[][] W) {
		int[][] SPM = new int[W.length][W.length];
		// �������dijkstra�㷨
		for (int i = 0; i < W.length; i++) {
			for (int j = i + 1; j < W.length; j++) {
				SPM[i][j] = dijkstra(W, i, j);
				SPM[j][i] = SPM[i][j];
			}
		}
		return SPM;
	}

	public static void main(String[] args) {
		/* ���㼯��V={v1,v2,������vn} */
		int[][] W = { { 0, 1, 3, 4 }, { 1, 0, 2, -1 }, { 3, 2, 0, 5 },
				{ 4, -1, 5, 0 } };
		int[][] W1 = { { 0, 1, 4, -1, -1, -1 }, { 1, 0, 2, 7, 5, -1 },
				{ 4, 2, 0, -1, 1, -1 }, { -1, 7, -1, 0, 3, 2 },
				{ -1, 5, 1, 3, 0, 6 }, { -1, -1, -1, 2, 6, 0 } };// ����һ��Ȩֵ����
		int[][] D = getShortestPathMatrix(W1);
		// ������Ľ��
		for (int i = 0; i < D.length; i++) {
			for (int j = 0; j < D[i].length; j++) {
				System.out.print(D[i][j] + " ");
			}
			System.out.println();
		}
	}
}