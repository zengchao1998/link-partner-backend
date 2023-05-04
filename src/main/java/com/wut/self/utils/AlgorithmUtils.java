package com.wut.self.utils;

import java.util.List;

/**
 * @author zeng
 * 算法工具类
 */
public class AlgorithmUtils {

    /**
     * 标签的最小编辑据离
     * @param label1 标签1
     * @param label2 标签2
     * @return 最小编辑距离
     */
    public static int minDistance(List<String> label1, List<String> label2) {
        int n = label1.size();
        int m = label2.size();

        if (n * m == 0) {
            return n + m;
        }

        int[][] d = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++) {
            d[i][0] = i;
        }

        for (int j = 0; j < m + 1; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int left = d[i - 1][j] + 1;
                int down = d[i][j - 1] + 1;
                int left_down = d[i - 1][j - 1];
                if (!label1.get(i - 1).equals(label2.get(j - 1))) {
                    left_down += 1;
                }
                d[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return d[n][m];
    }
}
