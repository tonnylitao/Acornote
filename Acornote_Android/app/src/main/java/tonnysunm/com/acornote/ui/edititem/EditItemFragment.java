package tonnysunm.com.acornote.ui.edititem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.realm.Realm;
import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.databinding.EdititemFragmentBinding;
import tonnysunm.com.acornote.model.Item;
import tonnysunm.com.acornote.ui.base.MVP;

public class EditItemFragment extends Fragment implements EditItemMVP.View {
    private static final String TAG = EditItemFragment.class.getSimpleName();

    @NonNull
    private int mFolderId;
    private String mColorName = "sky";

    @Nullable
    private Item mItem;     //update item

    @Override
    public void setPresenter(MVP.Presenter presenter) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edititem_fragment, container, false);

        final Bundle bundle = getActivity().getIntent().getExtras();
        mItem = bundle.getParcelable(EditItemActivity.EXTRA_ITEM);
        mFolderId = bundle.getInt(EditItemActivity.EXTRA_FOLDER_ID);
        mColorName = bundle.getString(EditItemActivity.EXTRA_COLOR_NAME);

        final EdititemFragmentBinding binding = EdititemFragmentBinding.bind(view);
        final EditItemViewModel viewModel = new EditItemViewModel(mItem, mColorName);
        viewModel.setView(this);

        binding.setViewModel(viewModel);

        return view;
    }

    @Override
    public void onCancel() {
        final Activity activity = getActivity();
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    @Override
    public void onSure(EditItemViewModel model) {
        final Context context = getContext();

        if (model.title == null) {
            Toast.makeText(context, R.string.need_item_title, Toast.LENGTH_SHORT).show();
            return;
        }

        final Realm.Transaction.OnSuccess onSuccess = () -> {
            final Activity activity = EditItemFragment.this.getActivity();
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        };

        final Realm.Transaction.OnError onError = (error) -> {
            Toast.makeText(context, R.string.create_error, Toast.LENGTH_SHORT).show();
        };

        if (mItem == null) {
            Item.create(model, mFolderId, onSuccess, onError);
        }else {
            Item.update(model, mItem.id, onSuccess, onError);
        }
    }

    @Override
    public void previous() {

    }

    @Override
    public void next() {

    }

    @Override
    public void add() {

    }
}