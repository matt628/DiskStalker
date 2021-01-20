package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsHandler {

    @Inject
    private ItemDataMapper itemDataMapper;

    @Inject
    private MainViewController mainViewController;

    public void updateStatistics(Root root) {
        List<Item> items = itemDataMapper.findAllByRoot(root);
        List<Type> types = getRootTypes(items);

        for (Type type : types) {
            long itemBytes = getItemBytes(items, type);
            type.setBytes(itemBytes);

            double itemPercentage = getItemPercentage(root, (double) itemBytes);
            type.setPercentage(itemPercentage);
        }

        ObservableList<Type> observableTypes = FXCollections.observableArrayList(types);
        mainViewController.updateStatisticsTable(observableTypes);
    }

    private double getItemPercentage(Root root, double itemBytes) {
        long rootSize = itemDataMapper.getRootItem(root).getSize();
        return BigDecimal.valueOf(itemBytes / rootSize * 100)
                .setScale(1, RoundingMode.HALF_DOWN)
                .doubleValue();
    }

    private List<Type> getRootTypes(List<Item> items) {
        return items.stream()
                .filter(Item::isFile)
                .map(Item::getType)
                .distinct()
                .collect(Collectors.toList());

    }

    private Long getItemBytes(List<Item> items, Type type) {
        return items.stream()
                .filter(item -> item.getType().equals(type))
                .map(Item::getSize)
                .reduce(0L, Long::sum);
    }

    public void cleanStatistics() {
        mainViewController.updateStatisticsTable(null);
    }
}
