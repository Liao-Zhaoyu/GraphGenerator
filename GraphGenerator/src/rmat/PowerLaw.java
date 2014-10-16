package rmat;


import java.util.Arrays;

/**
 *
 * @author renzo
 */
public class PowerLaw {

    public PowerLaw(){
        
    }
    
    public long[] getDistribution(int size, long max_degree, float k) {
        long[] array = new long[size];
        for (int i = 0; i < array.length; i++) {
            long r = 0;
            do {
                r = (long) (1.0 / Math.pow(1 - Math.random(), k));
            } while (r >= max_degree);

            array[i] = r;
        }

        reverseSort(array);
        return array;
    }

    public static void reverseSort(long a[]) {
        Arrays.sort(a);
        int j = 0;
        for (int i = a.length - 1; i >= 0 && i > j; i--) {
            long temp = a[i];
            a[i] = a[j];
            a[j] = temp;
            j++;
        }
    }
}
