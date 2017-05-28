package dominio;

import java.util.HashMap;

/**
 * Clase de todos los peleadores
 *
 */
public abstract class Peleador implements Peleable {

	private int salud;
	private int fuerza;
	private int defensa;
	private String nombre;
	private int nivel;
	private RandomGenerator rnd;

	/**
	 * Actualizar datos desde hashmap
	 * @param datos salud,fuerza,defensa,nombre,nivel
	 */
	protected void actualizar(final HashMap<String, Object> datos) {
		setSalud((Integer) datos.get("salud"));
		setFuerza((Integer) datos.get("fuerza"));
		setDefensa((Integer) datos.get("defensa"));
		setNombre((String) datos.get("nombre"));
		setNivel((Integer) datos.get("nivel"));
	}

	/**
	 * Devuelve hashmap con datos.
	 * @return datos en hashmap
	 */
	protected HashMap<String, Object> getTodo() {
		HashMap<String, Object> datos = new HashMap<>();
		datos.put("salud", getSalud());
		datos.put("fuerza", getFuerza());
		datos.put("defensa", getDefensa());
		datos.put("nombre", getNombre());
		datos.put("nivel", getNivel());
		return datos;
	}

	/**
	 * Constructor por defecto
	 * Carga myRamdom.
	 */
	public Peleador() {
		setRandomGenerator(new MyRandom());
	}

	/**
	 * Carga el generador de aleatorios.
	 * @param rg generador
	 */
	public void setRandomGenerator(final RandomGenerator rg) {
		this.rnd = rg;
	}

	/**
	 * Devuelve generador random en uso.
	 * @return randomgenerator
	 */
	public RandomGenerator getRandomGenerator() {
		return this.rnd;
	}

	/**
	 * Getter salud.
	 * @return salud
	 */
	public int getSalud() {
		return salud;
	}

	/**
	 * Setter Salud.
	 * @param salud salud
	 */
	protected void setSalud(final int salud) {
		this.salud = salud;
	}

	/**
	 * Getter Fuerza.
	 * @return fuerza
	 */
	public int getFuerza() {
		return fuerza;
	}

	/**
	 * Setter fuerza.
	 * @param fuerza fuerza
	 */
	protected void setFuerza(final int fuerza) {
		this.fuerza = fuerza;
	}

	/**
	 * Getter defensa.
	 * @return defensa
	 */
	public int getDefensa() {
		return defensa;
	}

	/**
	 * Setter defenza.
	 * @param defensa defensa
	 */
	protected void setDefensa(final int defensa) {
		this.defensa = defensa;
	}

	/**
	 * Getter nombre.
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Setter nombre.
	 * @param nombre nombre
	 */
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Getter nivel.F
	 * @return nivel
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * Setter nivel.
	 * @param nivel nivel
	 */
	protected void setNivel(final int nivel) {
		this.nivel = nivel;
	}

	/**
	 * Esta vivo.
	 * @return true si esta vivo
	 */
	public boolean estaVivo() {
		return salud > 0;
	}

	/**
	 * Accion despues de turno.
	 */
	public void despuesDeTurno() {
	}

	/**
	 * Da experiencia al personaje.
	 * @return nivel multiplicado
	 */
	public int otorgarExp() {
		return this.nivel * multiplicadorExperiencia();
	}

	/**
	 * Calcula el multiplicador de experiencia para otorgar EXP.
	 * @return multiplicador
	 */
	protected abstract int multiplicadorExperiencia();

	/**
	 * Recibir daño evaluando probabilidad de evasión y defensa
	 *
	 * @param dano daño inicial que el atacado sufrirá, puede verse modificado
	 * @return daño sufrido, es equivalente a cuánto disminuyó su salud
	 */
	public int serAtacado(final int dano) {
		int danoCalc = dano;
		if (rnd.nextDouble() >= probabilidadEvitarDanoEnAtaque()) {
			danoCalc -= defensaAlSerAtacado();
			danoCalc = quitarVidaSegunDano(danoCalc);
		}
		return 0;
	}

	/**
	 * Cacula probabilidad de evitar daño.
	 * @return probabilidad resultante.
	 */
	protected abstract double probabilidadEvitarDanoEnAtaque();

	/**
	 * Calcula defensa al ser atacado por otro peleador
	 * @return defensa resultante
	 */
	protected abstract int defensaAlSerAtacado();

	/**
	 * Disminuye la salud según el daño.
	 * @param dano daño a quitar al personaje
	 * @return     devuelve el daño causado si es mayor a cero;
	 *             caso contrario devuelve 0.
	 */
	protected int quitarVidaSegunDano(final int dano) {
		if (dano > 0) {
			salud -= dano;
			return dano;
		} else {
			return 0;
		}
	};

	/**
	 * Causar daño al atacado evaluando la probabilidad de golpe crítico y su ataque.
	 *
	 * @param atacado un objeto que implementa la interfaz Peleable, es aquel a ser atacado
	 * @return        daño causado.
	 */
	public int atacar(final Peleable atacado) {
		if (puedoAtacar(atacado.estaVivo())) {
			if (rnd.nextDouble() <= probabilidadGolpeCritico()) {
				return atacado.serAtacado(this.golpeCritico());
			} else {
				return atacado.serAtacado(getAtaque());
			}
		}
		return 0;
	}

	/**
	 * Daño de golpe critico.
	 * @return impacto de golpe critico
	 */
	protected abstract int golpeCritico();

	/**
	 * Calcula probabilida de efectuar un golpe critico.
	 * @return probabilidad de daño
	 */
	protected abstract double probabilidadGolpeCritico();

	/**
	 * Verifica si puede atacar.
	 * @param atacadoEstaVivo atacado es peleador vivo
	 * @return true si puede atacar
	 */
	protected boolean puedoAtacar(final boolean atacadoEstaVivo) {
		return true;
	}

	/**
	 * Indica si es afectado por un hechicero.
	 * @return true si es afectado;
	 *         false en caso contrario.
	 */
	public abstract boolean esAfectadoPorHechicero();

	/**
	 * Indica si es afectado por un guerrero.
	 * @return true si es afectado;
	 *         false en caso contrario.
	 */
	public abstract boolean esAfectadoPorGuerrero();
}
