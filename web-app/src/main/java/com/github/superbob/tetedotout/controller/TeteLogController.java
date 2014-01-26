package com.github.superbob.tetedotout.controller;

import com.github.superbob.tetedotout.dto.TeteLogDto;
import com.google.appengine.api.datastore.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/tete-log", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeteLogController {
    private final static String QUERY_LAST10 = "last10";

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<TeteLogDto> list(final @RequestParam(value = "q", required = false) String query) {
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        final Query q = new Query("TeteLogDto");
        q.addSort("begin", Query.SortDirection.DESCENDING);
        final PreparedQuery pq = ds.prepare(q);
        final Iterable<Entity> entities;
        if (QUERY_LAST10.equals(query)) {
            entities = pq.asIterable(FetchOptions.Builder.withLimit(10));
        } else {
            entities = pq.asIterable();
        }
        final List<TeteLogDto> teteLogDtoList = new ArrayList<TeteLogDto>();
        for (final Entity entity : entities) {
            teteLogDtoList.add(toTeteLogDto(entity));
        }
        return teteLogDtoList;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TeteLogDto> create(@RequestBody final TeteLogDto teteLogDto, final UriComponentsBuilder b) {
        final Entity entity = toEntity(teteLogDto);
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        final Transaction tx = ds.beginTransaction();
        final Key key = ds.put(entity);
        tx.commit();
        final UriComponents uriComponents = b.path("/tete-log/{id}").buildAndExpand(key.getId());
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());
        final ResponseEntity<TeteLogDto> teteLogDtoResponseEntity = new ResponseEntity<TeteLogDto>(toTeteLogDto(entity), httpHeaders, HttpStatus.CREATED);
        return teteLogDtoResponseEntity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<TeteLogDto> read(@PathVariable("id") final long id) {
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        final Key key = KeyFactory.createKey("TeteLogDto", id);
        ResponseEntity<TeteLogDto> teteLogDtoResponseEntity;
        try {
            final Entity entity = ds.get(key);
            teteLogDtoResponseEntity = new ResponseEntity<TeteLogDto>(toTeteLogDto(entity), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            teteLogDtoResponseEntity = new ResponseEntity<TeteLogDto>(HttpStatus.NOT_FOUND);
        }
        return teteLogDtoResponseEntity;
    }

    // TODO handle 200/201 statuses correctly when the resources already exists or not
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<TeteLogDto> update(@PathVariable("id") final long id, @RequestBody final TeteLogDto teteLogDto) {
        teteLogDto.setId(Long.valueOf(id));
        final Entity entity = toEntity(teteLogDto);
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        final Transaction tx = ds.beginTransaction();
        ds.put(entity);
        tx.commit();
        return new ResponseEntity<TeteLogDto>(toTeteLogDto(entity), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("id") final long id) {
        final Key key = KeyFactory.createKey("TeteLogDto", id);
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        final Transaction tx = ds.beginTransaction();
        ds.delete(key);
        tx.commit();
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private static final TeteLogDto toTeteLogDto(Entity entity) {
        final TeteLogDto teteLogDto = new TeteLogDto();
        teteLogDto.setId(entity.getKey().getId());
        teteLogDto.setBegin((Date) entity.getProperty("begin"));
        teteLogDto.setEnd((Date) entity.getProperty("end"));
        teteLogDto.setDurationSeconds((Long) entity.getProperty("durationSeconds"));
        teteLogDto.setMainLeft((Boolean) entity.getProperty("mainLeft"));
        teteLogDto.setSomeOther((Boolean) entity.getProperty("someOther"));
        return teteLogDto;
    }

    private static final Entity toEntity(TeteLogDto teteLogDto) {
        final Entity entity;
        if (teteLogDto.getId() != null) {
            final Key key = KeyFactory.createKey("TeteLogDto", teteLogDto.getId().longValue());
            entity = new Entity(key);
        } else {
            entity = new Entity("TeteLogDto");
        }
        entity.setProperty("begin", teteLogDto.getBegin());
        entity.setProperty("end", teteLogDto.getEnd());
        entity.setProperty("durationSeconds", teteLogDto.getDurationSeconds());
        entity.setProperty("mainLeft", teteLogDto.getMainLeft());
        entity.setProperty("someOther", teteLogDto.getSomeOther());

        return entity;
    }

}
