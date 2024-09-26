package com.example.toaextended.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public
enum FontStyle
{
	PLAIN("Plain", Font.PLAIN),
	BOLD("Bold", Font.BOLD),
	ITALIC("Italic", Font.ITALIC);

	private final String name;
	private final int font;

	@Override
	public String toString()
	{
		return name;
	}
}
