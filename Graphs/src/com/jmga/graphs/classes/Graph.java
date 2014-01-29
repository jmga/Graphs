package com.jmga.graphs.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import android.graphics.Color;
import android.util.Log;

public class Graph implements Cloneable {
	private final String[] ids = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i",
			"j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z" };

	public String getId(int i) {
		return ids[i];
	}

	private Hashtable<String, Integer[]> distancesT;
	private ArrayList<String> nombres;
	private Hashtable<String, Node> vertex;
	private ArrayList<Arrow> arrows;
	private Arrow aux;
	private int nodes;

	/*
	 * CONSTRUCTORES
	 * 
	 * CONSTRUCTOR NORMAL
	 */

	public Graph() {
		vertex = new Hashtable<String, Node>();
		arrows = new ArrayList<Arrow>();
		nombres = new ArrayList<String>();
		distancesT = new Hashtable<String, Integer[]>();
		nodes = 0;
	}

	/*
	 * CONSTRUCTOR ADICIONAL
	 */
	public Graph(Hashtable<String, Node> vertex, ArrayList<Arrow> arrows) {
		this.vertex = vertex;
		this.arrows = arrows;
		distancesT = new Hashtable<String, Integer[]>();

		nodes = vertex.size();
	}

	/*
	 * GETTERS AND SETTERS
	 * 
	 * NODES
	 */
	public Hashtable<String, Node> getVertex() {
		return vertex;
	}

	public Node getNode(String nombre) {
		return (Node) vertex.get(nombre);
	}

	public void setVertex(Hashtable<String, Node> vertex) {
		this.vertex = vertex;
	}

	public ArrayList<String> getNombres() {
		return nombres;
	}

	public void setNombres(ArrayList<String> nombres) {
		this.nombres = nombres;
	}

	public void addNode(Node node) {
		String nombre = ids[nodes];
		nombres.add(nombre);
		vertex.put(nombre, node);
		nodes++;
	}

	public void addNode(int x, int y) {
		String nombre = ids[nodes];
		nombres.add(nombre);
		Node node = new Node(x, y, nombre);
		vertex.put(nombre, node);
		nodes++;
	}

	public Node copiarNode(String name) {
		Node n = vertex.get(name);
		return n;

	}

	public void colorRestorationNodes() {
		Enumeration<Node> nodes = vertex.elements();
		while (nodes.hasMoreElements())
			((Node) (nodes.nextElement())).setColor(Color.BLACK);
	}

	public void setColorOfNode(String id, int color) {
		((Node) vertex.get(id)).setColor(color);
	}

	public void addNode(String string) {
		// TODO Auto-generated method stub
		Node node = new Node(0, 0, string);
		vertex.put(string, node);
		nodes++;
	}

	public void deleteNode(String name) {
		for (int i = 0; i < arrows.size(); i++) {
			Arrow a = arrows.get(i);
			if (a.getIdi().equals(name) || a.getIdf().equals(name) || a.getIdi() == null
					|| a.getIdi() == null) {
				deleteLink(a.getIdi(), a.getIdf());
				deleteLink(a.getIdf(),a.getIdi());
			//	arrows.remove(i);
				i = 0;
			}
		}
		for (int i = 0; i < nombres.size(); i++) {
			if (nombres.get(i).equals(name)) {
				nombres.remove(i);
				nombres.add(i, "nulo");
			}
		}
		Node n = new Node(0, 0, "nulo");
		vertex.put(name, n);
		nombres.remove("nulo");
	}

	/*
	 * ARROWS
	 */
	public void setArrows(ArrayList<Arrow> arrows) {
		this.arrows = arrows;
	}

	public ArrayList<Arrow> getArrows() {
		return arrows;
	}

	public void addLink(String idi, String idf, int weight) {
		Arrow a = new Arrow(idi, idf, weight);

		for (int i = 0; i < arrows.size(); i++) {
			Arrow aa = arrows.get(i);
			if (aa.getIdi().equals(idi)) {
				if (aa.getIdf().equals(idf)) {
					a = null;
				}
			}
			if (aa.getIdf().equals(idi)) {
				if (aa.getIdi().equals(idf)) {
					a = null;
				}
			}
		}

		if (a != null) {
			int i = buscarIndice(a.getWeight());

			if (i == -1)
				arrows.add(a);
			else
				arrows.add(i, a);

			vertex.get(idi).agregarEnlace(idf, weight);
			vertex.get(idf).agregarEnlace(idi, weight);
		}
	}

	public void deleteLink(String idi, String idf) {
		for (int i = 0; i < arrows.size(); i++) {
			Arrow a = arrows.get(i);
			if (a.getIdi().equals(idi)) {
				if (a.getIdf().equals(idf)) {
					vertex.get(idi).deleteEnlace(idf);
					vertex.get(idf).deleteEnlace(idi);
					arrows.remove(i);
					i = 0;
				}
			}
			if (a.getIdf().equals(idi)) {
				if (a.getIdi().equals(idf)) {
					vertex.get(idf).deleteEnlace(idi);
					vertex.get(idi).deleteEnlace(idf);
					arrows.remove(i);
					i = 0;
				}
			}
		}
	}

	public int compararPeso(Arrow a1, Arrow a2) {
		if (a1.getWeight() < a2.getWeight())
			return 1;
		if (a1.getWeight() > a2.getWeight())
			return 2;
		return 3;
	}

	public void changeWeight(String idi, String idf, int weight) {
		for (int i = 0; i < arrows.size(); i++) {
			Arrow a = arrows.get(i);
			if (mismaArista(a, buscarArista(idi, idf))) {
				a.setWeight(weight);
				ordenarArrows();
			}
		}
	}

	public boolean buscarArista(Arrow a) {
		for (int i = 0; i < arrows.size(); i++) {
			Arrow otro = arrows.get(i);
			if (a.getIdi().equals(otro.getIdi())
					&& a.getIdf().equals(otro.getIdf())
					&& a.getWeight() == otro.getWeight()) {
				arrows.remove(otro);
				return true;
			}
		}
		return false;
	}

	public void ordenarArrows() {
		for (int i = 0; i < arrows.size(); i++) {
			for (int j = 0; j < arrows.size(); j++) {
				switch (compararPeso(arrows.get(i), arrows.get(j))) {
				case 1:
					if (i > j) {
						arrows.add(j, arrows.get(i));
						arrows.remove(i + 1);
					}
					break;
				case 2:

					break;
				case 3:

					break;
				default:
					break;

				}
			}
		}
	}

	public Arrow buscarArista(String idi, String idf) {
		Arrow a = new Arrow(idi, idf, 0);
		return a;
	}

	public int buscarIndice(float peso) {
		for (int i = 0; i < arrows.size(); i++) {
			if (peso < arrows.get(i).getWeight())
				return i;
		}
		return -1;
	}

	public void update() {
		for (int i = 0; i < arrows.size(); i++) {
			String idi = arrows.get(i).getIdi();
			String idf = arrows.get(i).getIdf();
			Node iN = vertex.get(idi);
			Node iF = vertex.get(idf);
			arrows.get(i).start = new float[] { iN.getCenterX(),
					iN.getCenterY() };
			arrows.get(i).stop = new float[] { iF.getCenterX(), iF.getCenterY() };

		}
		for (Arrow aul : arrows) {
			Log.d("digimon", " Arista " + aul.getIdi() + "--" + aul.getIdf());
		}
		for (String naul : nombres) {
			for (int i = 0; i < vertex.get(naul).getEnlaces().size(); i++)
				Log.d("digimon",
						" Nodo " + naul + " enlace con "
								+ vertex.get(naul).getEnlaces().get(i).getIdf());
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public boolean mismaArista(Arrow a1, Arrow a2) {
		if (a1.getIdi().equals(a2.getIdi()) && a1.getIdf().equals(a2.getIdf())) {
			return true;
		} else if (a1.getIdf().equals(a2.getIdi())
				&& a1.getIdi().equals(a2.getIdf())) {
			return true;
		}
		return false;
	}

	public void updateDistances() {

	}
	
	public int getTotalWeight(){
		int total = 0;
		
		Iterator<Arrow> arrows_ = arrows.iterator();
		while(arrows_.hasNext()){
			total += arrows_.next().getWeight();
		}
		
		return total;
	}
	
	// return (n = 0) -> No regular
	// return (n > 0) -> regular
	public int isRegular(){
		int degree = 0;
		
		String[] seq = new String[nombres.size()];
		seq = getSequenceDegrees();
		
		return degree;
	}
	
	public String[] getSequenceDegrees(){
		String[] seq = new String[nombres.size()];
		
		
		Arrays.sort(seq);
		
		return seq;
	}

	public Arrow getAux() {
		return aux;
	}

	public void setAux(Arrow aux) {
		this.aux = aux;
	}
}
