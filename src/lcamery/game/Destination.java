package lcamery.game;

public class Destination {
	private String time;
	public final String DESTINATION;
	public final String CODE;
	private String gate;
	private int status;
	
	private Destination(String time, String dest, String code, String gate) {
		this.time = time;
		this.DESTINATION = dest;
		this.CODE = code;
		this.gate = gate;
		this.status = 0;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return this.status;
	}
	
	public String getTime() {
		return this.time;
	}
	
	public String getGate() {
		return this.gate;
	}
	
	public static class DestinationBuilder {
		private String time = null;
		private String dest = null;
		private String code = null;
		private String gate = null;
		
		public DestinationBuilder addTime(String time) {
			this.time = time;
			return this;
		}
		
		public DestinationBuilder addDest(String dest) {
			this.dest = dest;
			return this;
		}
		
		public DestinationBuilder addGate(String gate) {
			this.gate = gate;
			return this;
		}
		
		public DestinationBuilder addCode(String code) {
			this.code = code;
			return this;
		}
		
		public Destination build() {
			if (time == null) {
				String preh = "", prem = "";
				int hour = (int) (Math.random() * 18) + 6;
				if (hour < 10) {
					preh = "0";
				}
				int minutes = (int) (Math.random() * 60);
				if (minutes < 10) {
					prem = "0";
				}
				time = preh + hour + ":" + prem + minutes;
			}
			if (dest == null) {
				throw new IllegalArgumentException("No destination");
			}
			if (gate == null) {
				String gates = "ABCDEEFGHIJKLMNOP";
				int num = (int) (Math.random() * 50);
				if (num >= 10) {
					gate = gates.charAt((int)(Math.random() * gates.length())) + "" + num;
				} else {
					gate = gates.charAt((int)(Math.random() * gates.length())) + "0" + num;
				}
			}
			if (code == null) {
				throw new IllegalArgumentException("No code");
			}
			
			int num = (int)(Math.random()*2000) + 1000;
			return new Destination(time,dest.toUpperCase(),code + num,gate);
		}
	}

}
