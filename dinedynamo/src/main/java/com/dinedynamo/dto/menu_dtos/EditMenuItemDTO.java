package com.dinedynamo.dto.menu_dtos;

import com.dinedynamo.collections.menu_collections.MenuItem;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditMenuItemDTO
{
    String itemId;

    MenuItem menuItem;
}
