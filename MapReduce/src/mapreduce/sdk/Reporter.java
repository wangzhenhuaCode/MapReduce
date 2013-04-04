package mapreduce.sdk;

public interface Reporter {
	public long getCounter(Enum<?> key);
	public void incrCounter(Enum<?> key, long amount);
	public void setStatus(String status);
}
