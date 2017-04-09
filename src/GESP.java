import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Gaussian elimination with scaled partial pivoting yapan sinif :D
 */
public class GESP {

    /**
     * matrixim, o benim her seyim
     */
    private ArrayList<ArrayList<Double>> matrix = new ArrayList<>();
    /**
     * indexVector l olarak geciyor l[i]'ler falan kullaniliyor hatta.
     */
    private ArrayList<Integer> indexVector = new ArrayList<>();
    /**
     * sVector her satirdaki mutlak degerce en buyuk elemanlardan olusan arraylist
     */
    private ArrayList<Double> sVector = new ArrayList<>();
    /**
     * islemden gecmis row'larin bulundugu arraylist
     */
    private ArrayList<Integer> doneRows = new ArrayList<>();
    /**
     * matrisin satir sayisi
     */
    private int rowCount;

    /**
     * Dosya okuma, parcalayip matrix olusturma islemlerinin yapildigi metod
     * @param filename Cozulecek lineer sistemi virgullerle ayrilmis sekilde iceren dosya adi
     * @throws IOException
     */
    public void readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        try
        {
            String line;
            while( (line = reader.readLine()) != null)
            {
                String[] readChars = line.split(",");
                ArrayList<Double> row = new ArrayList<>();
                for(String s : readChars)
                {
                    Double num = Double.parseDouble(s);
                    row.add(num);
                }

                matrix.add(row);
            }
            rowCount = matrix.size();

        }
        catch(Exception ex)
        {
            System.out.println("Error:" + ex.getMessage());
        }
        finally {
            reader.close();
        }
    }

    /**
     * Yazilan tum yardimci metodlarin kullanildigi metod,
     * main gibi bir sey, asil is burda yapilmaya baslaniyor
     */
    public void solveTheSystem()
    {
        int k = 0; /* step */
        int j;


        /* Index vector filled */
        for(int i = 0; i < rowCount; i++)
            indexVector.add(i);

        /* s vector filled */
        for(int i = 0; i < rowCount; i++)
        {
            ArrayList<Double> row = matrix.get(i);
            Double max = findMax(row);
            if(max == 0) {
                System.out.println("The system has no solution");
                return;
            }
            sVector.add(max);
        }

        while(k < rowCount - 1)
        {
            j = findMaxRatio(k);
            int lj = indexVector.get(j);
            //lj = pivot row index
            int lk = indexVector.get(k);
            swapVector(j,k);
            //matrix.get(lj); //pivot row
            rowOperation(lj,k);
            k++;
        }
        /* backward substitution */
        ArrayList<Double> unknowns = new ArrayList<>();
        for(int i = 0; i < indexVector.size(); i++)
            unknowns.add(0.0);
        for(int i = indexVector.size(); i > 0; i--)
        {
            //get row
            ArrayList<Double> row = matrix.get(indexVector.get(i-1));
            Double sum = 0.0;
            for(int v = i; v < indexVector.size(); v++)
            {
                sum += row.get(v)*unknowns.get(v);
            }
            Double xn = (row.get(row.size()-1) - sum)/ row.get(i-1);
            unknowns.set(i-1,xn);
        }

       for(int i = 0; i < unknowns.size(); i++)
       {
           Double d = unknowns.get(i);
           long round = Math.round(d);
           Long l = round;
           double v = d - round;
           //cok fena yuvarliyodu bu sekilde bi sinir koydum
           if(Math.abs(v) < 0.0001) {
               unknowns.set(i, l.doubleValue());
           }
           else
               unknowns.set(i,d);
       }
       String s1 = "X = [ ";
       for(Double d: unknowns)
       {
           s1+=d+", ";
       }
       if(unknowns.size() >0)
           s1 = s1.substring(0,s1.length()-2);
       else
           s1 += "Ø";
       s1 += " ]";
       System.out.println(s1);
    }

    /**
     * Sutunda, pivot haricindeki elemanlari 0'lamak icin row operation'lari yapan metod.
     * @param pivot index of pivot
     * @param k step
     */
    private void rowOperation(Integer pivot, Integer k)
    {
        doneRows.add(pivot);
        for(int i = 0; i < rowCount; i++ )
        {
            if(!doneRows.contains(i))
            {
                Double value = matrix.get(i).get(k);
                Double pivotVal = matrix.get(pivot).get(k) / 1.0;
                Double multiplier = value/pivotVal * -1;

                Object o = matrix.get(pivot).clone();
                ArrayList<Double> pivotTempRow = (ArrayList<Double>)o;
                for(int v = 0; v < pivotTempRow.size(); v++)
                {
                    pivotTempRow.set(v,pivotTempRow.get(v)*multiplier);
                }
                ArrayList<Double> tempRow = matrix.get(i);
                ArrayList<Double> resultRow = new ArrayList<>();
                for(int v = 0; v < tempRow.size(); v++)
                {
                    resultRow.add(tempRow.get(v) + pivotTempRow.get(v));
                }
                matrix.set(i,resultRow);
            }
        }

    }

    /**
     * Index vector'un swap islemini gerceklestiren metod
     * @param j pivot
     * @param k step
     */
    private void swapVector(int j, int k) {
        if(j != k)
        {
            Integer lj = indexVector.get(j);
            Integer lk = indexVector.get(k);
            indexVector.set(j,lk);
            indexVector.set(k,lj);
        }
    }

    /**
     * Verilen satirdaki mutlak degerce en buyuk olan elemani bulan metod.
     * @param list Matrix'in row'u
     * @return O satirdaki, mutlak degerce en buyuk elemani dondurur.
     */
    private Double findMax(ArrayList<Double> list)
    {
        Double max = 0.0;
        for(int i = 0; i < list.size()-1; i++) //-1 because omitting augmented matrix
        {
            Double val  = Math.abs(list.get(i));
            if(val > max)
                max = val;
        }
        return max;
    }

    /**
     * findMaxRatio metodunun icinde kullanilan yardimci metod
     * @param list Icinde ratio'larin bulundugu arraylist
     * @return max ratio'nun index'ini dondurur
     */
    private Integer findMaxRatioIndex(ArrayList<Double> list)
    {
        Double max = Collections.max(list);
        return list.indexOf(max);
    }

    /**
     * a[li][k]/s[li] lerden ratio'su en yuksek olanin index'ini dondurur
     * @param k step
     * @return Ratio'su en yuksek olan degerin index'i
     */
    private Integer findMaxRatio(int k)
    {
        ArrayList<Double> column = getColumn(k);
        ArrayList<Double> ratioResults = new ArrayList<>();
        for(int i = k; i < rowCount; i++)
        {
            int li = indexVector.get(i);
            Double val = Math.abs(column.get(li));
            Double denominator = (sVector.get(li)/1.0);
            Double result = val / denominator;
            ratioResults.add(result);
        }
        Integer maxRatioIndex = findMaxRatioIndex(ratioResults);
        return maxRatioIndex+k; /* index 0 1 2 iken li 2 3 4 ler bunun dengelemesi +k ile saglaniyor */
    }

    /**
     * Indexi verilen sutunu arraylist halinde elde etmeye yarayan metod
     * @param index Sutun indexi
     * @return Indexi verilen sutundaki elemanlarla dolu bir arraylist dondurur.
     * */
    private ArrayList<Double> getColumn(Integer index)
    {
        ArrayList<Double> column = new ArrayList<>();
        for(int i = 0; i < rowCount; i++)
        {
            Double val = matrix.get(i).get(index);
            column.add(val);
        }
        return column;
    }
}
