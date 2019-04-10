package com.zc.bean;

public class Point {
    public float point_x = 0;
    public float point_y = 0;
    public int flag = -1;

    public void setPoint_x(float point_x) {
        this.point_x = point_x;
    }

    public void setPoint_y(float point_y) {
        this.point_y = point_y;
    }

    public float getPoint_x() {
        return point_x;
    }

    public float getPoint_y() {
        return point_y;
    }

    @Override
    public String toString() {
        return "(" + point_x +
                "," + point_y +
                ')';
    }
}
