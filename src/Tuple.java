import java.net.InetAddress;

// a mini helper class
class Tuple {
    public InetAddress ipAddress;
    public long TTL;
    public Tuple (InetAddress ipAddress, long TTL) {
        this.ipAddress = ipAddress;
        this.TTL = TTL;
    }
}

