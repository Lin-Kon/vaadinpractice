package com.example.vaadinpractice.ui;

import com.example.vaadinpractice.model.Movie;
import com.example.vaadinpractice.service.MovieService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import sun.security.validator.ValidatorException;

import java.text.NumberFormat;
import java.util.Locale;

@Route("movie")
//@Theme(value = Lumo.class)
public class MovieView extends VerticalLayout {
    private MovieService movieService;
    private Binder<Movie> movieBinder;
    private Grid<Movie> movieGrid;

    public MovieView(MovieService movieService)
//    public MovieView()
    {
        this.movieService = movieService;

        movieBinder = new Binder<>();

        TextField idField = new TextField("Movie ID");
        TextField titleField = new TextField("Movie Title");
        Button submitButton = new Button("Sumbit");
        Button updateButton = new Button("Update");

        FormLayout formLayout = new FormLayout();
        formLayout.add(idField, titleField, submitButton, updateButton);

        StringToIntegerConverter plainIntegerConverter = new StringToIntegerConverter("ID must be a number") {
            protected java.text.NumberFormat getFormat(Locale locale) {
                NumberFormat format = super.getFormat(locale);
                format.setGroupingUsed(false);
                return format;
            };
        };

        movieBinder
                .forField(idField)
                .asRequired()
                .withValidator(id -> id.length() == 4, "ID must be 4 digit long")
                .withConverter(plainIntegerConverter)
                .withValidator(id -> id > 999, "ID must be four digit long")
                .bind(movie -> Math.toIntExact(movie.getId()), (movie, id) -> movie.setId(id));

        movieBinder
                .forField(titleField)
                .asRequired()
                .bind(Movie::getTitle, Movie::setTitle);

        movieGrid = new Grid<>();
        movieGrid
                .addColumn(Movie::getId)
                .setWidth("150px")
                .setFlexGrow(0)
                .setHeader("Movie ID");
        movieGrid
                .addColumn(Movie::getTitle)
                .setFlexGrow(1)
                .setHeader(getStyleHeader("Movie Title"));
        movieGrid
                .addComponentColumn(movie -> getEditColumn(movie))
                .setWidth("50px")
                .setFlexGrow(0);
        movieGrid
                .addComponentColumn(movie -> getDeleteColumn(movie))
                .setWidth("50px")
                .setFlexGrow(0);
        movieGrid.setItems(movieService.findAll());
        movieGrid.setItems(new Movie(1000, "The American Virgin"));

        add(formLayout, movieGrid);

        submitButton.addClickListener(event -> {
            Movie movie = new Movie();
            try{
                movieBinder.writeBean(movie);
                Notification.show(movie.toString());
                Movie savedMovie = movieService.save(movie);
                movieGrid.setItems(movieService.findAll());
                Notification.show("Saved "+savedMovie.getTitle());
            } catch (Exception e){
                Notification.show(e.getMessage());
            }
        });

        updateButton.addClickListener(event -> {
            Movie movie = new Movie();
            try {
                movieBinder.writeBean(movie);
                Notification.show(movie.toString());
                Movie updatedMovie = movieService.update(movie);
                movieGrid.setItems(movieService.findAll());
                Notification.show("Updated " + updatedMovie.getTitle());
            } catch (Exception e){
                Notification.show(e.getMessage());
            }
        });
    }

    private Component getEditColumn(Movie movie) {
        Button button = new Button();
        button.setIcon(VaadinIcon.EDIT.create());
        button.getElement().setProperty("title", "This is a edit button");
        button.addClickListener(event -> {
            movieBinder.readBean(movie);
        });
        return button;
    }

    private Component getStyleHeader(String text) {
        Span header = new Span(text);
        header
                .getStyle()
                .set("background-color","blue")
                .set("border-width", "3px");
        return header;
    }

    private Component getDeleteColumn(Movie movie) {
        Button button = new Button();
        button.setIcon(VaadinIcon.FILE_REMOVE.create());
        button.getElement().setProperty("title", "This is a delete button");
        button.addClickListener(event -> {
            movieService.delete(movie);
            movieGrid.setItems(movieService.findAll());
        });
        return button;
    }
}
