package mainapp;

public interface Lockable {
    void lock();
    void unlock();
    boolean isLocked();
}
