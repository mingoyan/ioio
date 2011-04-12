package ioio.lib.new_impl;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.new_impl.IncomingState.InputPinListener;
import android.util.Log;

public class AnalogInputImpl extends AbstractPin implements AnalogInput, InputPinListener {
	private int value_;
	private boolean valid_ = false;
	
	AnalogInputImpl(IOIOImpl ioio, int pin) throws ConnectionLostException {
		super(ioio, pin);
	}
	
	@Override
	public float getVoltage() throws InterruptedException, ConnectionLostException {
		return read() * getReference();
	}

	@Override
	public float getReference() {
		return 3.3f;
	}

	@Override
	synchronized public void setValue(int value) {
		Log.i("DigitalInputImpl", "Pin " + pinNum_ + " value is " + value);
		assert(value >= 0 || value < 1024);
		value_ = value; 
		if (!valid_) {
			valid_ = true;
			notifyAll();
		}
	}

	@Override
	synchronized public float read() throws InterruptedException, ConnectionLostException {
		checkState();
		while (!valid_) {
			wait();
		}
		return (float) value_ / 1023.0f;
	}

}