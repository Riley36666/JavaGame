public class LevelData {

    /**
     * Each int[] represents a platform as:
     * { x, y, width, height }
     */
    public static int[][] getLevel(int level) {
        switch (level) {

            // --- Level 1: Tutorial ---
            case 1:
                return new int[][]{
                        {0, 680, 1280, 40},    // floor
                        {200, 600, 150, 20},
                        {400, 520, 150, 20},
                        {650, 450, 150, 20},
                        {900, 400, 150, 20},
                };

            // --- Level 2: Gentle climb ---
            case 2:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {150, 600, 120, 20},
                        {350, 550, 120, 20},
                        {550, 500, 120, 20},
                        {750, 450, 120, 20},
                        {950, 400, 120, 20},
                };

            // --- Level 3: Tight jumps ---
            case 3:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {200, 600, 80, 20},
                        {350, 540, 80, 20},
                        {500, 480, 80, 20},
                        {650, 420, 80, 20},
                        {800, 360, 80, 20},
                        {950, 300, 80, 20},

                };

            // --- Level 4: Gaps and drops ---
            case 4:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {200, 580, 100, 20},
                        {500, 620, 100, 20},
                        {800, 550, 100, 20},
                        {1100, 480, 100, 20},

                };

            // --- Level 5: Vertical movement ---
            case 5:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {300, 580, 120, 20},
                        {300, 500, 120, 20},
                        {300, 420, 120, 20},
                        {600, 400, 120, 20},
                        {900, 360, 120, 20},

                };

            // --- Level 6: Wide jumps ---
            case 6:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {250, 600, 100, 20},
                        {500, 530, 100, 20},
                        {800, 460, 100, 20},
                        {1100, 390, 100, 20},

                };

            // --- Level 7: Narrow ledges ---
            case 7:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {150, 610, 60, 20},
                        {300, 550, 60, 20},
                        {450, 490, 60, 20},
                        {600, 430, 60, 20},
                        {750, 370, 60, 20},
                        {900, 310, 60, 20},
                        {1050, 250, 60, 20},

                };

            // --- Level 8: Zig-zag ---
            case 8:
                return new int[][]{
                        {0, 680, 1280, 40},
                        {150, 600, 150, 20},
                        {400, 520, 150, 20},
                        {650, 600, 150, 20},
                        {900, 520, 150, 20},
                        {1150, 440, 150, 20},

                };

            // --- Level 9: Long endurance ---
            case 9:
                return new int[][]{
                        {0, 680, 1920, 40},
                        {300, 600, 100, 20},
                        {600, 540, 100, 20},
                        {900, 480, 100, 20},
                        {1200, 420, 100, 20},
                        {1500, 360, 100, 20},

                };

            // --- Level 10: Final challenge ---
            case 10:
                return new int[][]{
                        {0, 680, 1920, 40},
                        {250, 600, 80, 20},
                        {450, 520, 80, 20},
                        {650, 440, 80, 20},
                        {850, 360, 80, 20},
                        {1050, 280, 80, 20},
                        {1250, 200, 80, 20},
                        {1450, 140, 80, 20},

                };

            // --- Default: fallback (flat) ---
            default:
                return new int[][]{
                        {0, 680, 1280, 40}
                };
        }
    }

    /**
     * Each int[] represents a win block as:
     * { x, y, width, height }
     */
    public static int[][] wincon(int level) {
        switch (level) {
            case 1: return new int[][]{{1100, 330, 100, 20}};
            case 2: return new int[][]{{1150, 330, 100, 20}};
            case 3: return new int[][]{{1100, 220, 80, 20}};
            case 4: return new int[][]{{1300, 380, 100, 20}};
            case 5: return new int[][]{{1150, 320, 100, 20}};
            case 6: return new int[][]{{1350, 330, 100, 20}};
            case 7: return new int[][]{{1200, 170, 60, 20}};
            case 8: return new int[][]{{1350, 360, 100, 20}};
            case 9: return new int[][]{{1750, 300, 100, 20}};
            case 10: return new int[][]{{1650, 80, 80, 20}};
            default: return new int[][]{{0, 0, 0, 0}};
        }
    }
}
