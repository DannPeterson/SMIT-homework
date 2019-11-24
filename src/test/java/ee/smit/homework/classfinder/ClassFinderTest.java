package ee.smit.homework.classfinder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;


import org.hamcrest.core.IsNull;

import org.junit.Before;
import org.junit.Test;

public class ClassFinderTest {
	private ClassLoader classloader;

	@Before
	public void setUp() {
		classloader = Thread.currentThread().getContextClassLoader();
	}

	@Test
	public void givenCorrectFile_whenGetMatches_thenGetMatches() throws URISyntaxException {
		URI fileURI = classloader.getResource("classes.txt").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> result = classFinder.getMatches(fileName, "bar");
		
		assertThat(result, is(IsNull.notNullValue()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyFile_whenGetMatches_thenIllegalArgumentException() throws URISyntaxException {
		URI fileURI = classloader.getResource("empty.txt").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> result = classFinder.getMatches(fileName, "pattern");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenWrongFileType_whenGetMatches_thenIllegalArgumentException() throws URISyntaxException {
		URI fileURI = classloader.getResource("wrongtype.png").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> result = classFinder.getMatches(fileName, "pattern");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void givenWrongFilePath_whenGetMatches_thenIllegalArgumentException() throws URISyntaxException {
		ClassFinder classFinder = new ClassFinder();
		List<String> result = classFinder.getMatches("", "pattern");
	}
	
	@Test
	public void givenNoSpaceCamelcasePattern_whenGetMatches_thenGetMatches() throws URISyntaxException {
		List<String> expected = new ArrayList<>();
		expected.add("a.b.FooBarBaz");
		expected.add("c.d.FooBar");
		
		URI fileURI = classloader.getResource("classes.txt").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> actual = classFinder.getMatches(fileName, "FoBa");
		
		assertThat(expected, equalTo(actual));
	}
	
	@Test
	public void givenNoSpaceNoCamelcasePattern_whenGetMatches_thenGetMatches() throws URISyntaxException {
		List<String> expected = new ArrayList<>();
		expected.add("a.b.FooBarBaz");
		
		URI fileURI = classloader.getResource("classes.txt").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> actual = classFinder.getMatches(fileName, "fbb");
		
		assertThat(expected, equalTo(actual));
	}
	
	@Test
	public void givenSpaceNoCamelcasePattern_whenGetMatches_thenGetMatches() throws URISyntaxException {
		List<String> expected = new ArrayList<>();
		expected.add("c.d.FooBar");
		
		URI fileURI = classloader.getResource("classes.txt").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> actual = classFinder.getMatches(fileName, "FBar ");
		
		assertThat(expected, equalTo(actual));
	}
	
	@Test
	public void givenPatternWithWildcard_whenGetMatches_thenGetMatches() throws URISyntaxException {
		List<String> expected = new ArrayList<>();
		expected.add("a.b.FooBarBaz");
		
		URI fileURI = classloader.getResource("classes.txt").toURI();
		String fileName = Paths.get(fileURI).toString();
		ClassFinder classFinder = new ClassFinder();
		List<String> actual = classFinder.getMatches(fileName, "B*rBaz");
		
		assertThat(expected, equalTo(actual));
	}
}
