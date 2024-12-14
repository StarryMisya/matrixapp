import java.io.*;
import java.util.*;

class MatrixUtils {

    public static double[][] readMatrixFromFile(String fileName) throws IOException {
        List<double[]> matrix = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("\\s+");
                double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                matrix.add(row);
            }
        }
        return matrix.toArray(new double[0][]);
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.printf("%8.2f", value);
            }
            System.out.println();
        }
    }
}

class MatrixOperations {

    public static double[][] add(double[][] a, double[][] b) {
        validateSameDimensions(a, b);
        int rows = a.length, cols = a[0].length;
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

    public static double[][] subtract(double[][] a, double[][] b) {
        validateSameDimensions(a, b);
        int rows = a.length, cols = a[0].length;
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }

    public static double[][] multiply(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            throw new IllegalArgumentException("Количество столбцов первой матрицы должно совпадать с количеством строк второй.");
        }
        int rows = a.length, cols = b[0].length, common = a[0].length;
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < common; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static double determinant(double[][] matrix) {
        int size = matrix.length;
        if (size == 1) {
            return matrix[0][0];
        }
        if (size == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        double det = 0;
        for (int i = 0; i < size; i++) {
            double[][] subMatrix = createSubMatrix(matrix, i);
            det += Math.pow(-1, i) * matrix[0][i] * determinant(subMatrix);
        }
        return det;
    }

    private static double[][] createSubMatrix(double[][] matrix, int excludeColumn) {
        int size = matrix.length;
        double[][] subMatrix = new double[size - 1][size - 1];
        for (int i = 1; i < size; i++) {
            int subColIndex = 0;
            for (int j = 0; j < size; j++) {
                if (j == excludeColumn) continue;
                subMatrix[i - 1][subColIndex++] = matrix[i][j];
            }
        }
        return subMatrix;
    }

    private static void validateSameDimensions(double[][] a, double[][] b) {
        if (a.length != b.length || a[0].length != b[0].length) {
            throw new IllegalArgumentException("Матрицы должны иметь одинаковые размеры.");
        }
    }
}

public class MatrixApp {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите путь к файлу первой матрицы: ");
            String firstMatrixFile = scanner.nextLine();
            System.out.print("Введите путь к файлу второй матрицы: ");
            String secondMatrixFile = scanner.nextLine();

            double[][] matrix1 = MatrixUtils.readMatrixFromFile(firstMatrixFile);
            double[][] matrix2 = MatrixUtils.readMatrixFromFile(secondMatrixFile);

            System.out.println("\nПервая матрица:");
            MatrixUtils.printMatrix(matrix1);

            System.out.println("\nВторая матрица:");
            MatrixUtils.printMatrix(matrix2);

            System.out.println("\nРезультат сложения:");
            MatrixUtils.printMatrix(MatrixOperations.add(matrix1, matrix2));

            System.out.println("\nРезультат вычитания:");
            MatrixUtils.printMatrix(MatrixOperations.subtract(matrix1, matrix2));

            System.out.println("\nРезультат умножения:");
            MatrixUtils.printMatrix(MatrixOperations.multiply(matrix1, matrix2));

            System.out.println("\nОпределитель первой матрицы: " + MatrixOperations.determinant(matrix1));
            System.out.println("Определитель второй матрицы: " + MatrixOperations.determinant(matrix2));
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка в операциях с матрицами: " + e.getMessage());
        }
    }
}