package br.game.handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Content {
	
	private HashMap<String, Texture> textures;
	private HashMap<String, Music> musics;
	private HashMap<String, Sound> sounds;
	
	public Content() {
		textures = new HashMap<String, Texture>();
		musics   = new HashMap<String, Music>();
		sounds   = new HashMap<String, Sound>();
	}
	
	private String getKeyFromPath(String path) {
		int slashIndex = path.lastIndexOf('/');
		String key;
		if(slashIndex == -1) {
			key = path.substring(0, path.lastIndexOf('.'));
		}
		else {
			key = path.substring(slashIndex + 1, path.lastIndexOf('.'));
		}
		return key;
	}
	
	
	/***********/
	/* Texture */
	/***********/
	
	/**
	 * Loads the texture with the file name as the key.
	 * The file format is not considered;
	 * @param path - path to image (root is assets folder)
	 */
	public void loadTexture(String path) {		
		loadTexture(path, getKeyFromPath(path) );
	}
	
	/**
	 * Loads the texture
	 * @param path - path to image (root is assets folder)
	 * @param key  - key to access image
	 */
	public void loadTexture(String path, String key) {
		Texture tex = new Texture(path);
		textures.put(key, tex);
	}
	
	/**
	 * Return the texture of the given key
	 * @param key - key of the texture
	 * @return Texture
	 */
	public Texture getTexture(String key) {
		return textures.get(key);
	}
	
	/**
	 * Dispose of the texture and remove it from the HashMap
	 * @param key - key of the texture to remove
	 */
	public void removeTexture(String key) {
		Texture tex = textures.get(key);
		if(tex != null) {
			textures.remove(key);
			tex.dispose();
		}
	}
	
	/*********/
	/* Music */
	/*********/
	
	public void loadMusic(String path) {
		loadMusic(path, getKeyFromPath(path));
	}
	
	public void loadMusic(String path, String key) {
		Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
		musics.put(key, m);
	}
	
	public Music getMusic(String key) {
		return musics.get(key);
	}
	
	public void removeMusic(String key) {
		Music m = musics.get(key);
		if(m != null) {
			musics.remove(key);
			m.dispose();
		}
	}
	/*******/
	/* SFX */
	/*******/
	
	public void loadSound(String path) {
		loadSound(path, getKeyFromPath(path));
	}
	
	public void loadSound(String path, String key) {
		Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(key, s);
	}
	
	public Sound getSound(String key) {
		return sounds.get(key);
	}
	
	public void removeSound(String key) {
		Sound sound = sounds.get(key);
		if(sound != null) {
			sounds.remove(key);
			sound.dispose();
		}
	}
	
	/**********/
	/* Others */
	/**********/

	public void removeAll() {
		for( Texture tex : textures.values() ) {
			tex.dispose();
		}
		textures.clear();
		for( Music m : musics.values() ) {
			m.dispose();
		}
		musics.clear();
		for( Sound s : sounds.values() ) {
			s.dispose();
		}
		sounds.clear();
	}
}
