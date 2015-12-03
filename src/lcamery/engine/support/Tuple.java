package lcamery.engine.support;

public class Tuple<A, B, C> {
	public A t;
	public B r;
	public C x;
	
	public Tuple(A t, B r, C x) {
		this.t = t;
		this.r = r;
		this.x = x;
	}
	
	public static class TupleBuilder<T,R,X> {
		private T t = null;
		private R r = null;
		private X x = null;
		
		public TupleBuilder<T,R,X> addT(T t) {
			this.t = t;
			return this;
		}
		
		public TupleBuilder<T,R,X> addR(R r) {
			this.r = r;
			return this;
		}
		
		public TupleBuilder<T,R,X> addX(X x) {
			this.x = x;
			return this;
		}
		
		public Tuple<T, R, X> build() {
			return new Tuple<T, R, X>(t,r,x);
		}
	}

}
