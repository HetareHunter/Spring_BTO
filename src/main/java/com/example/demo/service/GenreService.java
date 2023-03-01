package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Genre;
import com.example.demo.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GenreService
{
	@Autowired
	private GenreRepository genreRepository;

	public List<Genre> sortGenreRepository()
	{
		return genreRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}
}
