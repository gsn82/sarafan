package com.gorshkov.sarafan.controller;


import com.gorshkov.sarafan.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")
public class MessageController {
    private int counter = 4;


    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>() {{
        add(new HashMap<String, String>() {{ put("id", "1"); put("text", "First message"); }});
        add(new HashMap<String, String>() {{ put("id", "2"); put("text", "Second message"); }});
        add(new HashMap<String, String>() {{ put("id", "3"); put("text", "Third message"); }});
    }};

    //  выводим информацию ввиди json
    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    // выводим информацию по json
    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getMessage(id);
    }
    // выводим информацию по json
    private Map<String, String> getMessage(@PathVariable String id) {
        // сипользуем лямду
        return messages.stream()
                .filter(message -> message.get("id").equals(id))  // фильтруем по id
                .findFirst()                                      // выводим первого
                .orElseThrow(NotFoundException::new);             // если не нашли отправляем отбойник
    }

    // добавляем новые данные
    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        // добавляем  id
        message.put("id", String.valueOf(counter++));
        // добаляем сообщение
        messages.add(message);

        return message;
    }

    // обновление информации
    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDb = getMessage(id);

        messageFromDb.putAll(message);
        messageFromDb.put("id", id);

        return messageFromDb;
    }

    // удяляем по id
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);

        messages.remove(message);
    }
}
