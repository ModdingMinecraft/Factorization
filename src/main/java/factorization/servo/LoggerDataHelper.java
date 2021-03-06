package factorization.servo;

import java.io.IOException;

import factorization.api.datahelpers.DataHelper;
import factorization.api.datahelpers.MergedDataHelper;
import factorization.api.datahelpers.Share;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class LoggerDataHelper extends MergedDataHelper {
    ServoMotor motor;
    public boolean hadError = false;
    
    public LoggerDataHelper(ServoMotor motor) {
        this.motor = motor;
    }
    
    @Override
    protected boolean shouldStore(Share share) {
        return false;
    }

    @Override
    public boolean isReader() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false; // Hmm. Not sure!
    }

    @Override
    protected <E> E putImplementation(E o) throws IOException {
        return o;
    }
    
    @Override
    public void log(String message) {
        motor.putError(message);
        hadError = true;
    }
}
