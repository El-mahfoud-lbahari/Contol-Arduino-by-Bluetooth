package com.mahtiz.controlarduino;

interface Counter {
    void onTick(long millisUntilFinished, String message);

    void onFinish();
}
