package mapreduce.node.logic;

import java.io.Serializable;

public class Job implements Serializable {
	private String jobId;
	private String[] input;
	private String output;
	private String initNodeHostName;
	private Integer initNodePort;
	private Integer reduceNumber;
	private Integer nodeNumber;
}
