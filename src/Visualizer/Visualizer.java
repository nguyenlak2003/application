
package Visualizer;

import Algorithm.Sort;
import Background.Bar;
import Background.ColorManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Visualizer
{
    private static final int PADDING = 20;
    private static final int MaxBarHeight = 350, MinBarHeight = 30;
    private Integer[] array;
    private int capacity, speed;
    private Bar[] bars;
    private boolean isHasArray, wasSorted;
    
    private long startTime, time;
    private int swapping;
    
    private Color originalColor, wasSortedColor, comparingColor;
    
    private BufferStrategy bs;
    private Graphics g;

    SortedListener listener;
    
    public Visualizer(int capacity, int fps, SortedListener listener)
    {
        this.capacity = capacity;
        this.speed = (int) (1000.0 / fps);
        this.listener = listener;
        
        this.originalColor = ColorManager.BAR_WHITE;
        this.wasSortedColor = ColorManager.BAR_BLUE;
        this.comparingColor = ColorManager.BAR_YELLOW;
        
        bs = this.listener.getBufferStrategy();
        isHasArray = false;
        wasSorted = true;
    }
    
    public void createRandomArray(int canvasWidth, int canvasHeight)
    {
        array = new Integer[capacity];
        bars = new Bar[capacity];
        isHasArray = true;
        wasSorted = false;
        
        // initial position
        double x = PADDING;
        int y = canvasHeight - PADDING;
        
        // width of each bar
        double width = (double) (canvasWidth - 2 * PADDING) / capacity;
        
        // get graphics
        g = bs.getDrawGraphics();
        // clear screen
        g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        
        // generate random array
        Random Rand = new Random();
        int value;
        Bar bar;
        for(int i = 0; i < array.length; i++){
            value = Rand.nextInt(MaxBarHeight) + MinBarHeight;
            array[i] = value;
            
            bar = new Bar((int) x, y, (int) width, array[i], this.originalColor);
            bar.draw(g);
            bars[i] = bar;
            
            // move to next position
            x += width;
        }
        
        bs.show();
        g.dispose();
    }
    
    // Bubble sort
    public void bubbleSort()
    {
        if(!isHasArray || wasSorted)
            return;
        
        g = bs.getDrawGraphics();
        
        // calculate elapsed time
        startTime = (int) System.nanoTime();
        Sort.bubbleSort(array.clone());
        time = (int) (System.nanoTime() - startTime);

        swapping = 0;
        for(int i = array.length - 1; i >= 0; i--)
        {
            for(int j = 0; j < i; j++)
            {
                colorCompare(j, j + 1, comparingColor);
                if(array[j] > array[j + 1])
                {
                    swap(j, j + 1);
                    swapping++;
                }
            }
            
            changeColor(i, wasSortedColor);
        }
        listener.onArraySorted(time, swapping);
        g.dispose();
        wasSorted = true;
    }

    // Selection sort
    public void selectionSort()
    {
        if(!isHasArray || wasSorted)
            return;
        
        g = bs.getDrawGraphics();
        
        // calculate elapsed time
        startTime = (int) System.nanoTime();
        Sort.bubbleSort(array.clone());
        time = (int) (System.nanoTime() - startTime);
        
        swapping = 0;
        for(int i = array.length - 1; i >= 0; i--)
        {
            int pos = 0;
            bars[pos].setColor(ColorManager.BAR_RED);
            bars[pos].draw(g);
            bs.show();
            
            for(int j = 1; j <= i; j++)
            {
                changeColor(j, comparingColor);
                delay();
                changeColor(j, originalColor);
                
                if(array[j] > array[pos])
                {
                    changeColor(pos, originalColor);
                    changeColor(j, ColorManager.BAR_RED);
                    pos = j;
                }
            }
            swap(pos, i);
            swapping++;
            changeColor(pos, originalColor);
            changeColor(i, wasSortedColor);
        }
        listener.onArraySorted(time, swapping);
        g.dispose();
        wasSorted = true;
    }
    
    // Insertion sort
    public void insertionSort()
    {
        if(!isHasArray || wasSorted)
            return;
        
        g = bs.getDrawGraphics();
        
        // calculate elapsed time
        startTime = (int) System.nanoTime();
        Sort.bubbleSort(array.clone());
        time = (int) (System.nanoTime() - startTime);
        
        swapping = 0;
        for(int i = array.length - 1; i >= 0; i--)
        {
            int pos = i;
            bars[pos].setColor(ColorManager.BAR_RED);
            bars[pos].draw(g);
            bs.show();
            
            for(int j = i; j < array.length; j++)
            {
                if(array[i] > array[j])
                    pos = j;
            }
            
            for(int j = i + 1; j <= pos; j++)
            {
                swap(j - 1, j);
                swapping++;
            }
            
            changeColor(pos, ColorManager.BAR_BLUE);
        }
        listener.onArraySorted(time, swapping);
        g.dispose();
        wasSorted = true;
    }
    
    // Quick Sort
    public void quickSort()
    {   
        if(!isHasArray || wasSorted)
            return;
        
        g = bs.getDrawGraphics();
        
        // calculate elapsed time
        startTime = (int) System.nanoTime();
        Sort.bubbleSort(array.clone());
        time = (int) (System.nanoTime() - startTime);
        
        swapping = 0;
        quickSort(0, array.length - 1);
        listener.onArraySorted(time, swapping);
        g.dispose();
        wasSorted = true;
    }
    
    public void quickSort(int start, int end)
    {
       if(start == end)
       {
           changeColor(end, wasSortedColor);
           return;
       }
       if(start < end)
       {
           int pivot = array[end];
           int pos = start - 1;
           
           bars[end].setColor(ColorManager.BAR_GREEN);
           bars[end].draw(g);
           bs.show();
           
           for(int i = start; i < end; i++)
           {
               if(array[i] < pivot)
               {
                   pos++;
                   swap(pos, i);
                   swapping++;
               }
           }
           
           pos++;
           bars[end].setColor(wasSortedColor);
           swap(pos, end);
           swapping++;
           
           if(start == end)
           {
               for(int i = start; i <= end; i++)
               {
                   bars[i].setColor(wasSortedColor);
                   bars[i].draw(g);
                   bs.show();
               }
           }
           
           quickSort(start, pos - 1);
           quickSort(pos + 1, end);
       }
    }
    
    // Merge Sort
    public void mergeSort()
    {
        if(!isHasArray || wasSorted)
            return;
        
        g = bs.getDrawGraphics();
        
        startTime = (int) System.nanoTime();
        Sort.bubbleSort(array.clone());
        time = (int) (System.nanoTime() - startTime);
        
        swapping = 0;
        mergeSort(0, array.length - 1);
        listener.onArraySorted(time, swapping);
        g.dispose();
        wasSorted = true;
    }
    
    public void mergeSort(int start, int end)
    {
        if(start >= end) 
            return;
        
        int mid = (start + end) / 2;
        
        mergeSort(start, mid);
        mergeSort(mid + 1, end);
        
        merge(start, end);
    }
    
    public void merge(int start, int end)
    {   
        int mid = (start + end) / 2;
        Color midColor = getBarColor(mid);
        
        int n1 = mid - start + 1;
        int[] left = new int[n1];
        for(int i = 0; i < left.length; i++)
            left[i] = array[i + start];

        int n2 = end - mid;
        int[] right = new int[n2];
        for(int i = 0; i < right.length; i++)
            right[i] = array[i + mid + 1];
        
        int i = 0, j = 0;
        int pos = start;
        
        while(i < n1 && j < n2)
        {
            bars[pos].clear(g);
            if(left[i] < right[j])
            {
                array[pos] = left[i];
                bars[pos].setValue(array[pos]);
                i++;
            }
            else
            {
                array[pos] = right[j];
                bars[pos].setValue(array[pos]);
                j++;
            }
            swapMerge(pos, midColor);
            pos++;
            swapping++;
        }
        
        while(i < n1)
        {
            bars[pos].clear(g);
            
            array[pos] = left[i];
            bars[pos].setValue(array[pos]);
            swapMerge(pos, midColor);
            pos++;
            i++;
            swapping++;
        }
        
        while(j < n2)
        {
            bars[pos].clear(g);
            
            array[pos] = right[j];
            bars[pos].setValue(array[pos]);
            swapMerge(pos, midColor);
            pos++;
            j++;
            swapping++;
        }
         
    }
    
    public void drawArray()
    {
        if(!isHasArray)
            return;
        
        g = bs.getDrawGraphics();
        
        for (Bar bar : bars)
            bar.draw(g);
         
        bs.show();
        g.dispose();  
    }
    
    private void swap(int i, int j) {
        int temporary = array[i];
        array[i] = array[j];
        array[j] = temporary;
        
        Color color = bars[i].getColor();
        bars[i].setColor(bars[j].getColor());
        bars[j].setColor(color);
        
        bars[i].clear(g);
        bars[j].clear(g);
        
        bars[j].setValue(array[j]);
        bars[i].setValue(array[i]);
        
        colorCompare(i, j, comparingColor);
    }
    
    public void colorCompare(int i, int j, Color color)
    {
        Color colors[] = {bars[i].getColor(), bars[j].getColor()};
        
        if(bars[i].getColor() == originalColor) 
        {
            bars[i].setColor(color);
        }
        bars[i].draw(g);
        
        if(bars[j].getColor() == originalColor)
        {
           bars[j].setColor(color);
        }
        bars[j].draw(g);
        
        bs.show();
        
        delay();
        
        bars[i].setColor(colors[0]);
        bars[i].draw(g);
        
        bars[j].setColor(colors[1]);
        bars[j].draw(g);
        
        bs.show();
        
    }
    
    public void changeColor(int i, Color color)
    {
        bars[i].setColor(color);
        bars[i].draw(g);
        bs.show();
    }
    
    public void delay()
    {
        try{
            TimeUnit.MILLISECONDS.sleep(speed);
        } catch(InterruptedException ex){}
    }
    
    public void swapMerge(int pos, Color wasSorted)
    {
        bars[pos].setColor(comparingColor);
        bars[pos].draw(g);
        bs.show();

        delay();

        bars[pos].setColor(wasSorted);
        bars[pos].draw(g);
        bs.show();
    }
    
    // return a color for a bar
    private Color getBarColor(int value)
    {
            int interval = (int) (array.length / 5.0);
            if (value < interval)
                    return ColorManager.BAR_ORANGE;
            else if (value < interval * 2)
                    return ColorManager.BAR_YELLOW;
            else if (value < interval * 3)
                    return ColorManager.BAR_BLUE;
            else if (value < interval * 4)
                    return ColorManager.BAR_CYAN;
            return ColorManager.BAR_GREEN;

    }

    public void setCapacity(int value)
    {
        this.capacity = value;
    }
    
    public void setFPS(int value)
    {
        this.speed = (int) (1000.0 / value);
    }
    public interface SortedListener
    {
        void onArraySorted(long elapsedTime, int swapping);
        BufferStrategy getBufferStrategy();
    }
}
