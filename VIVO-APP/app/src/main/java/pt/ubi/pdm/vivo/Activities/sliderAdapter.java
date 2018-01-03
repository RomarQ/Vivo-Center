package pt.ubi.pdm.vivo.Activities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ubi.pdm.vivo.R;

public class sliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;


    public sliderAdapter(Context context) {
        this.context = context;
    }


    // activity images
    public int [] images_list = {
            R.drawable.activity_1,
            R.drawable.activity_2,
            R.drawable.activity_3,
            R.drawable.activity_4,
            R.drawable.activity_5,
            R.drawable.activity_6,
    };

    // activity titles
    public String[] titles_list = {
            "Corrumpit vituperata",
            "Ne nam solum",
            "Vis elitr ludus homero",
            "Aliquip nostrum id",
            "Eam eleifend reprehendunt et",
            "Ut brute primis noluisse eum"
    };

    // activity descriptions
    public String[] descriptions_list = {
            "Eu quem alienum mnesarchum usu, his te omnium maiorum sadipscing. An percipit " +
            "sensibus efficiantur his, id natum dicam elaboraret pri, cum at etiam inimicus " +
            "inciderint. Ex timeam fuisset probatus sea. Eam tacimates sensibus ea. At nihil " +
            "putent incorrupte ius, et accumsan intellegam eam." +
            "Has et posse porro eos cetero percipitur id, usu malis graeci in. Ut etiam oporteat" +
            "assentior duo.No debitis voluptaria vix, simul detracto molestiae vim ad. Ad vim " +
            "picurei rationibus, at nec moderatius honestatis. " +
            "Adhuc graeci bonorum cum ut, malis scripta ne cum, libris voluptatibus ex sea. " +
            "Cu impedit principes omittantur per, mei ne epicurei laboramus. At ceteros persequeris vix.",

            "Cu melius scribentur qui, viris eleifend quaerendum ne qui, ea qui elaboraretae. " +
            "Eu has facilisis concludaturque. Est eu stet legimus omnesque, ius ad illud zril " +
            "primis, novum fuisset volutpat sit ad. Tollit propriae nec ei, nam an docendi intellegam, illum antiopam eam no.\n" +
            "No vidisse nostrud gubergren cum, ad modo graece persius cum, et dictas explicari eum. " +
            "Per ad aliquando persecuti necessitatibus, ornatus appellantur has id.At sea mundi " +
            "menandri, pri tempor accusata philosophia cu. Prima definiebas cotidieque cu mei, " +
            "usu antiopam accusamus ad. Eos in tale ridens conceptam, oportere vituperata ei mea, " +
            "esse ponderum deseruisse te ius. Mea quem erat at, vix te illud ullum sapientem. Vis id justo oblique. ",

            "Graeci expetenda explicari cum ea, tamquam pericula sit id.Erant quando pri at. Munere quidam te eos, vis lorem lobortis referrentur id. Veri mandamus efficiantur duo ut, ius admodum neglegentur ut, ne cum altera repudiandae definitiones. No quo vide vidit corpora, quod diceret forensibus an mel. Sit ullum ornatus sapientem an, tation essent corrumpit sea ut. Mea vitae molestie recteque ei.\n" +
            "Cu tale iusto aliquid mel. Sed ne maiorum propriae prodesset. Cu deleniti appellantur referrentur vim, adhuc brute ocurreret ad nec, simul civibus reprimique ei nec. Mei errem equidem maluisset ex. Meis insolens theophrastus sit at, at tractatos partiendo suavitate pro. Ea sed sint nostrud prodesset.",

            "Sit sint facer epicuri, ad semper consetetur omittantur vim.Quo id tantas aeterno placerat, te cum solet graece facilisis.Quis amet purto duo ad. Pri ut autem tation imperdiet, est ut maiestatis definitiones. Impetus aeterno suscipit ad has. Sit aliquam nusquam periculis eu, id mel iusto partem.Dolore meliore at mel, no error latine nec." +
            "Prima perfecto ex sea. Mel vide gubergren id, mei fuisset temporibus ea, vim meliore erroribus consectetuer cu. Doming possim ex sed, eam detracto consulatu at, vis movet habemus cu. Est porro eligendi dissentiet ad, harum iisque dolores eos ei. Pro cu causae nostrum dolores.",

            "Mel nibh partem mediocrem an, et legimus albucius ius, pro ne facilis tractatos. Ne nemore vidisse apeirian sea. Utamur placerat.Prima ullum oblique pro te, his solum phaedrum dissentiet ex, pro nominavi atomorum incorrupte id. Nam cu homero pericula, eam laudem repudiare voluptaria id. Timeam eleifend vim ne, no primis mentitum per. Mea ea audiam ancillae, minimum officiis persequeris eam cu.\n" +
            "Wisi solum audire nam ei, has ex nonumy option. Cu audiam aperiam habemus vix, choro virtute fabulas cu usu. Et oportere qualisque sit, eam ea error detracto percipit. Sed tale habeo tempor at. Movet torquatos neglegentur te est. Iisque delenit an pro. Ipsum nemore nostrud eu sed, saperet perfecto nam an, dicam quando voluptatibus te usu. Quo et minim lobortis.",

            "Diam legendos ius in, et iusto laudem petentium vel, ad unum tantas eleifend nam. Dictas noluisse.Erant quando pri at. Munere quidam te eos, vis lorem lobortis referrentur id. Veri mandamus efficiantur duo ut, ius admodum neglegentur ut, ne cum altera repudiandae definitiones. No quo vide vidit corpora, quod diceret forensibus an mel.\n" +
            "Sit ullum ornatus sapientem an, tation essent corrumpit sea ut. Mea vitae molestie recteque ei. Cu tale iusto aliquid mel. Sed ne maiorum propriae prodesset. Cu deleniti appellantur referrentur vim, adhuc brute ocurreret ad nec, simul civibus reprimique ei nec. Mei errem equidem maluisset ex. Meis insolens theophrastus sit at, at tractatos partiendo suavitate pro. Ea sed sint nostrud prodesset.",

    };


    @Override
    public int getCount() {
        return images_list.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_slider, container, false);


        // Just some navigation indicators, to help user on getting a better experience
        ImageView left_arrow = (ImageView) view.findViewById(R.id.left_arrow);
        left_arrow.setImageResource(R.drawable.ic_chevron_left_black_24dp);
        if ( position == 0 )
            left_arrow.setImageResource(0);

        ImageView right_arrow = (ImageView) view.findViewById(R.id.right_arrow);
        TextView info = (TextView) view.findViewById(R.id.TV_arrasteInfo);
        if ( position == getCount()-1 ) {
            info.setText("Fim");
            right_arrow.setImageResource(0);
        }

        // Setting up each slider information
        ImageView imageSlide = (ImageView) view.findViewById(R.id.IV_slider);
        TextView titleSlide = (TextView) view.findViewById(R.id.TV_slider_title);
        TextView descriptionSlide = (TextView) view.findViewById(R.id.TV_slider_description);
        imageSlide.setImageResource(images_list[position]);
        titleSlide.setText(titles_list[position]);
        descriptionSlide.setText(descriptions_list[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Every time user swaps to next slider, remove previous
        container.removeView((NestedScrollView)object);
    }
}
