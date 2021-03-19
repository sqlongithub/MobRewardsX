package me.sql.mobrewardsx.util;

public class VersionUtil {

	// TODO: Improve code quality
	
	private static int[] getIntKeys(String version) {
		String[] _versionKeys = version.substring(version.indexOf("MC:")+4,version.lastIndexOf('.')+2).split("\\.");
		int[] versionKeys = new int[_versionKeys.length];
		for(int i = 0; i<_versionKeys.length; i++) {
			versionKeys[i] = Integer.parseInt(_versionKeys[i]);
		}
		return versionKeys;
	}
	
	public static boolean versionGreaterThan(String version, String otherVersion) {
		int[] version1Keys = getIntKeys(version);
		String[] _version2Keys = otherVersion.split("\\.");
		int[] version2Keys = new int[_version2Keys.length];
		for(int i = 0; i<_version2Keys.length; i++) {
			version2Keys[i] = Integer.parseInt(_version2Keys[i]);
		}
		if(version1Keys[1]>version2Keys[1]) {
			return true;
		} else if(version1Keys[1]==version2Keys[1]) {
			if(version1Keys.length<3 && version2Keys.length<3) {
				return false;
			}
			if(version1Keys[2]>version2Keys[2]) {
				return true;
			} else if(version1Keys.length>version2Keys.length) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean versionLessThan(String version, String otherVersion) {
		int[] version1Keys = getIntKeys(version);
		int[] version2Keys = getIntKeys(otherVersion);
		if(version1Keys[1]<version2Keys[1]) {
			return true;
		} else if(version1Keys[1]==version2Keys[1]) {
			if(version1Keys.length<3 && version2Keys.length<3) {
				return false;
			}
			if(version1Keys[2]<version2Keys[2]) {
				return true;
			} else if(version1Keys.length<version2Keys.length) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean versionEqualTo(String version, String otherVersion) {
		int[] version1Keys = getIntKeys(version);
		int[] version2Keys = getIntKeys(otherVersion);
		if(version1Keys[1]==version2Keys[1]) {
			if(version1Keys.length<3 && version2Keys.length<3) {
				return true;
			}
			if(version1Keys[2]==version2Keys[2]) {
				return true;
			}
		}
		return false;
	}
	
}
