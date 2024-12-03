package me.sergioramirez.days.day05;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMapping {
    private String sourceCategory;
    private String detinationCategory;
    private List<Transformation> transformations;

    public Long calcule_destination(Long prevDestination) {
        Optional<Transformation> in_range = transformations.stream().filter(t -> prevDestination >= t.getSource() && prevDestination <= (t.getSource()+t.getOffset()-1)).findFirst();
        if(in_range.isEmpty())
            return prevDestination;
        Transformation transformation = in_range.get();
        return prevDestination-transformation.getSource()+transformation.getDestination();
    }
}
