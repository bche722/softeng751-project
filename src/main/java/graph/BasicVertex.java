/**
 * 
 */
package graph;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public class BasicVertex implements Vertex {
	
	private String name;
	private int weight;
	
	public BasicVertex(String name) {
		this.name = name;
	}
	
	public BasicVertex(String name, int weight) {
		this(name);
		this.weight = weight;
	}
	
	/* (non-Javadoc)
	 * @see graph.Vertex#name()
	 */
	public String name() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see graph.Vertex#setName(java.lang.String)
	 */
	public void setName(String name) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException("Invalid name supplied");
		} else {
			this.name = name;
		}
	}

	/* (non-Javadoc)
	 * @see graph.Vertex#toXML()
	 */
	public String toXML() {
		StringBuffer b = new StringBuffer();
		b.append("<node id=\"" + this.name +"\">");
		b.append("<attr name=\" name\"><string>");
        b.append(this.name);
        b.append("</string></attr>");
        b.append("</node>");
		return b.toString();
	}
	
	public Vertex clone() {
		Vertex retval = new BasicVertex(this.name);
		return retval;
	}
	
	public int weight() {
		return weight;
	}

	public void setWeight(int w) {
		this.weight = w;
	}

	//public int compareWeight(Vertex c) {
	//	return (new Integer(this.weight)).compareTo(c.weight());
	//}	
	
	public int compareWeight(Vertex c) {
		if (this.weight > c.weight())
			return 1;
		else if (this.weight == c.weight())
			return 0;
		else
			return -1;
	}	
}
