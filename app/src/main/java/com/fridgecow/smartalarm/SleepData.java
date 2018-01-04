package com.fridgecow.smartalarm;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 04/01/18.
 */

public class SleepData extends ArrayList<DataRegion> {
    private static final String TAG = SleepData.class.getSimpleName();
    private double mStart;
    private double mEnd;

    public SleepData(double start, double end){
        mStart = start;
        mEnd = end;
    }

    public SleepData(double start, double end, List<DataRegion> list){
        super(list);

        mStart = start;
        mEnd = end;
    }

    public SleepData(Context c, String filename) throws IOException{
        readIn(c, filename);
    }

    public void writeOut(Context c, String filename) throws IOException {
        final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(c.openFileOutput(filename, 0)));

        out.write("tracking,"+this.getStart()+","+this.getEnd()+"\n");
        for(DataRegion d : this){
            out.write(d.getLabel()+","+d.getStart()+","+d.getEnd()+"\n");
            Log.d(TAG, d.getLabel()+","+d.getStart()+","+d.getEnd());
        }
        out.close();
    }

    public void readIn(Context c, String filename) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(c.openFileInput(filename)));

        //First line
        String line = in.readLine();
        if(line != null) {
            String[] meta = line.split(",");
            mStart = Double.parseDouble(meta[1]);
            mEnd = Double.parseDouble(meta[2]);
        }else{
            throw new IOException("SleepData file has no contents");
        }

        //Other lines
        while((line = in.readLine()) != null){
            String[] data = line.split(",");
            if(data.length == 3) {
                this.add(new DataRegion(Double.parseDouble(data[1]), Double.parseDouble(data[2]), data[0]));
            }
        }

        in.close();
    }
    public double getStart() {
        return mStart;
    }

    public void setStart(double mStart) {
        this.mStart = mStart;
    }

    public double getEnd() {
        return mEnd;
    }

    public void setEnd(double mEnd) {
        this.mEnd = mEnd;
    }
}
